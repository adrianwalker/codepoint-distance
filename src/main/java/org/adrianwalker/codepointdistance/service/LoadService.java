package org.adrianwalker.codepointdistance.service;

import java.io.IOException;
import java.io.InputStream;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.adrianwalker.codepointdistance.LoadRequest;
import org.adrianwalker.codepointdistance.LoadResponse;
import org.adrianwalker.codepointdistance.dataaccess.RedisDataAccess;
import org.adrianwalker.codepointdistance.dataaccess.S3DataAccess;
import org.adrianwalker.codepointdistance.io.CodePointZipInputStream;
import org.adrianwalker.codepointdistance.model.CodePoint;

public final class LoadService {

  private static final Logger LOGGER = Logger.getLogger(LoadService.class.getName());

  private final S3DataAccess s3DataAccess;
  private final RedisDataAccess redisDataAccess;

  public LoadService(final S3DataAccess s3DataAccess, final RedisDataAccess redisDataAccess) {

    this.s3DataAccess = s3DataAccess;
    this.redisDataAccess = redisDataAccess;
  }

  public LoadResponse load(final LoadRequest request) {

    LOGGER.log(INFO, format("request = %s", request.toString()));

    boolean success;
    String message;

    try (InputStream is = s3DataAccess.readObject(request)) {

      loadObject(is);
      drain(is);

      success = true;
      message = "load complete";

    } catch (final Exception e) {

      LOGGER.log(SEVERE, e.getMessage(), e);

      success = false;
      message = e.getMessage();
    }

    return new LoadResponse().setSuccess(success).setMessage(message);
  }

  private void loadObject(final InputStream is) throws IOException, TimeoutException, ExecutionException, InterruptedException {

    CodePointZipInputStream cpzis = new CodePointZipInputStream(new ZipInputStream(is));

    ZipEntry entry;
    while (null != (entry = cpzis.getNextCsvEntry())) {

      LOGGER.log(INFO, format("entry = %s", entry.getName()));

      wait(loadCodePoints(cpzis.parse()));
    }
  }

  private List<Future<Long>> loadCodePoints(final Iterable<CodePoint> codePoints) throws IOException {

    List<Future<Long>> futures = new ArrayList<>();

    long count = 0;
    for (CodePoint codePoint : codePoints) {

      futures.add(redisDataAccess.write(codePoint));

      if (++count % 1000 == 0) {
        redisDataAccess.flush();
      }

      redisDataAccess.flush();
    }

    return futures;
  }

  private void wait(final List<Future<Long>> futures) throws InterruptedException, ExecutionException, TimeoutException {

    for (Future<Long> future : futures) {
      future.get(5, TimeUnit.SECONDS);
    }
  }

  public void drain(final InputStream is) throws IOException {

    is.readAllBytes();
  }
}
