package org.adrianwalker.codepointdistance.dataaccess;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public final class DataAccessFactory {

  private static final String ELASTICACHE_HOST = "ELASTICACHE_HOST";
  private static final String ELASTICACHE_PORT = "ELASTICACHE_PORT";

  private DataAccessFactory() {
  }

  public static S3DataAccess createS3DataAccess() {

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
      .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
      .build();

    return new S3DataAccess(s3Client);
  }

  public static RedisDataAccess createRedisDataAccess() {

    StatefulRedisConnection<String, String> redisConnection = RedisClient.create(RedisURI.Builder
      .redis(System.getenv(ELASTICACHE_HOST))
      .withPort(Integer.valueOf(System.getenv(ELASTICACHE_PORT)))
      .build())
      .connect();

    return new RedisDataAccess(redisConnection);
  }
}
