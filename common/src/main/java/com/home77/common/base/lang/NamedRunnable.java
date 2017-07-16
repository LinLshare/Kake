package com.home77.common.base.lang;

/**
 * Runnable implementation which always sets its thread name.
 */
public abstract class NamedRunnable implements Runnable {
  protected final String mThreadName;

  public NamedRunnable(String format, Object... args) {
    mThreadName = String.format(format, args);
  }

  @Override
  public final void run() {
    String oldName = Thread.currentThread().getName();
    Thread.currentThread().setName(mThreadName);
    try {
      execute();
    } finally {
      Thread.currentThread().setName(oldName);
    }
  }

  protected abstract void execute();
}
