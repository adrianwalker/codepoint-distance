package org.adrianwalker.codepointdistance.dataaccess;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import java.util.Map;
import java.util.concurrent.Future;
import org.adrianwalker.codepointdistance.model.CodePoint;
import org.adrianwalker.codepointdistance.model.Postcode;

public final class RedisDataAccess {

  private static final String NORTHINGS = "northings";
  private static final String EASTINGS = "eastings";

  private final StatefulRedisConnection<String, String> redisConnection;

  public RedisDataAccess(final StatefulRedisConnection<String, String> redisConnection) {

    this.redisConnection = redisConnection;
  }

  public RedisDataAccess setAutoFlush(final boolean autoFlush) {

    redisConnection.setAutoFlushCommands(autoFlush);

    return this;
  }

  public void flush() {

    redisConnection.flushCommands();
  }

  public CodePoint read(final Postcode postcode) {

    Postcode formattedPostcode = postcode.format();

    Map<String, String> value = hgetall(formattedPostcode.toString());

    if (value.isEmpty()) {
      return null;
    }

    long eastings = Long.valueOf(value.get(EASTINGS));
    long northings = Long.valueOf(value.get(NORTHINGS));

    return new CodePoint()
      .setPostcode(formattedPostcode)
      .setEastings(eastings)
      .setNorthings(northings);
  }

  public Future<Long> write(final CodePoint codePoint) {

    Postcode formattedPostcode = codePoint.getPostcode().format();

    String eastings = String.valueOf(codePoint.getEastings());
    String northings = String.valueOf(codePoint.getNorthings());

    Map<String, String> value = Map.of(
      EASTINGS, eastings,
      NORTHINGS, northings);

    return hset(formattedPostcode.toString(), value);
  }

  private Map<String, String> hgetall(final String key) {

    return redisConnection.sync().hgetall(key);
  }

  private RedisFuture<Long> hset(final String key, final Map<String, String> value) {

    return redisConnection.async().hset(key, value);
  }
}
