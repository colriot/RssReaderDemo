package io.github.colriot.rssreaderdemo;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class NetworkException extends RuntimeException {
  private final int code;

  public NetworkException(int code, String message) {
    super("Http failure: " + message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
