package com.home77.common.protocol.base;

import com.home77.common.protocol.ErrorCode;

@SuppressWarnings("unchecked")
public abstract class AbstractEntry<T> {
  public AbstractEntry() {
  }

  private transient int mCode;
  private transient String mMsg;

  // getter

  public int code() {
    return mCode;
  }

  public String msg() {
    return mMsg;
  }

  // setter

  public T success() {
    mCode = ErrorCode.SUCCESS;
    mMsg = "success";
    return (T) this;
  }

  public T status(int code, String msg) {
    mCode = code;
    mMsg = msg;
    return (T) this;
  }
}
