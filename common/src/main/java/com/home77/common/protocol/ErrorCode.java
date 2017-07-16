package com.home77.common.protocol;

/**
 * Global error code definition
 */
public final class ErrorCode {
  public static final int SUCCESS = 0;
  public static final int SERVER_INTERNAL_ERROR = 1000;
  public static final int INVALID_REQUEST_BODY = 2000;
  public static final int INVALID_REQUEST_PARAM = 2001;
  public static final int INVALID_SIGN_INFO = 2002;

  public static final int PERMISSION_DENY = 2003;
  public static final int INVALIDATE_TOKEN = 2004;
}
