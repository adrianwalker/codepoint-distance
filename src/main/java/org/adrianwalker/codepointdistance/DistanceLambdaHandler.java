package org.adrianwalker.codepointdistance;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.InputStream;
import java.io.OutputStream;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import org.adrianwalker.codepointdistance.dataaccess.DataAccessFactory;
import org.adrianwalker.codepointdistance.dataaccess.RedisDataAccess;
import org.adrianwalker.codepointdistance.rest.CodePointResource;
import org.adrianwalker.codepointdistance.service.DistanceService;

public final class DistanceLambdaHandler implements RequestStreamHandler {

  private static final Logger LOGGER = Logger.getLogger(DistanceLambdaHandler.class.getName());

  private static JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

  public DistanceLambdaHandler() {

    RedisDataAccess redisDataAccess = DataAccessFactory.createRedisDataAccess().setAutoFlush(true);

    ResourceConfig resourceConfig
      = new ResourceConfig()
        .registerInstances(new CodePointResource(
          new DistanceService(redisDataAccess)))
        .register(JacksonFeature.class);

    handler = JerseyLambdaContainerHandler.getAwsProxyHandler(resourceConfig);
  }

  @Override
  public void handleRequest(final InputStream is, final OutputStream os, final Context context) {

    try (os) {

      handler.proxyStream(is, os, context);

    } catch (final IOException ioe) {

      LOGGER.log(SEVERE, ioe.getMessage(), ioe);
    }
  }
}
