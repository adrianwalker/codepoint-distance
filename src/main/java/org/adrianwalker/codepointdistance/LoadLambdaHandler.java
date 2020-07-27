package org.adrianwalker.codepointdistance;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.adrianwalker.codepointdistance.dataaccess.DataAccessFactory;
import org.adrianwalker.codepointdistance.dataaccess.RedisDataAccess;
import org.adrianwalker.codepointdistance.dataaccess.S3DataAccess;
import org.adrianwalker.codepointdistance.service.LoadService;

public final class LoadLambdaHandler implements RequestHandler<LoadRequest, LoadResponse> {

  private static LoadService loadService;

  public LoadLambdaHandler() {

    S3DataAccess s3DataAccess = DataAccessFactory.createS3DataAccess();
    RedisDataAccess redisDataAccess = DataAccessFactory.createRedisDataAccess().setAutoFlush(false);

    loadService = new LoadService(s3DataAccess, redisDataAccess);
  }

  @Override
  public LoadResponse handleRequest(final LoadRequest request, final Context context) {

    return loadService.load(request);
  }
}
