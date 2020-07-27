package org.adrianwalker.codepointdistance.dataaccess;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import java.io.InputStream;
import org.adrianwalker.codepointdistance.LoadRequest;

public final class S3DataAccess {

  private final AmazonS3 s3Client;

  public S3DataAccess(final AmazonS3 s3Client) {

    this.s3Client = s3Client;
  }

  public InputStream readObject(final LoadRequest request) {

    GetObjectRequest s3Request = new GetObjectRequest(request.getBucket(), request.getKey());

    return s3Client.getObject(s3Request).getObjectContent();
  }
}
