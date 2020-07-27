package org.adrianwalker.codepointdistance;

public final class LoadRequest {

  private String bucket;
  private String key;

  public String getBucket() {
    return bucket;
  }

  public LoadRequest setBucket(final String bucket) {
    this.bucket = bucket;
    return this;
  }

  public String getKey() {
    return key;
  }

  public LoadRequest setKey(final String key) {
    this.key = key;
    return this;
  }

  @Override
  public String toString() {
    return "bucket=" + bucket + ", key=" + key;
  }
}
