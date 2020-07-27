package org.adrianwalker.codepointdistance.service;

import org.adrianwalker.codepointdistance.dataaccess.RedisDataAccess;
import org.adrianwalker.codepointdistance.model.CodePoint;
import org.adrianwalker.codepointdistance.model.Postcode;
import org.adrianwalker.codepointdistance.distance.DistanceAlgorithm;

public final class DistanceService {

  private final RedisDataAccess redisDataAccess;

  public DistanceService(final RedisDataAccess redisDataAccess) {

    this.redisDataAccess = redisDataAccess;
  }

  public CodePoint read(final Postcode postcode) {

    return redisDataAccess.read(postcode);
  }

  public double distance(
    final DistanceAlgorithm distance,
    final CodePoint fromCodePoint,
    final CodePoint toCodePoint) {

    return distance.calculate(fromCodePoint, toCodePoint);
  }
}
