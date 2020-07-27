package org.adrianwalker.codepointdistance;

public final class LoadResponse {

  private boolean success;
  private String message;

  public boolean isSuccess() {
    return success;
  }

  public LoadResponse setSuccess(final boolean success) {
    this.success = success;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public LoadResponse setMessage(final String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return "success=" + success + ", message=" + message;
  }
}
