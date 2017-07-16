package com.home77.common.base.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;

import com.home77.common.base.debug.Check;
import com.home77.common.base.util.RuntimeInfoHelper;

import java.util.ArrayList;

public class BaseHandler {
  // Context Runnable

  /**
   * If your runnable logic depend on context, use this instead of
   * {@link Runnable} to capture the context. When run, you can use captured
   * context regardless of {@link ContextManager#activity()}, or check your
   * captured context is invalid.
   */
  public static abstract class ActivityRunnable implements Runnable {
    public final Activity activity = ContextManager.activity();
  }

  // State Machine

  private static final int STATE_OPENED = 1;
  private static final int STATE_CLOSING = 2;
  private static final int STATE_CLOSED = 3;

  static int sState = STATE_CLOSED;

  static Runnable sClosingWaiter;

  public static void open() {
    switch (sState) {
      case STATE_OPENED:
        Check.d(false);
        break;

      case STATE_CLOSING:
        sHandler.removeCallbacks(sClosingWaiter);
        sClosingWaiter = null;
        break;

      case STATE_CLOSED:
        sHandler = new Handler(Looper.getMainLooper());
        if (Build.VERSION.SDK_INT >= 16) {
          try {
            sAnimationHandler = new AnimationHandler();
          } catch (Throwable ignored) {
            sAnimationHandler = null;
          }
        }
        break;
    }
    sState = STATE_OPENED;
  }

  public static void close() {
    close(null, false);
  }

  public static void close(Runnable done) {
    close(done, true);
  }

  private static void close(final Runnable done, boolean waitForBlock) {
    if (sState == STATE_OPENED) {
      sState = STATE_CLOSING;

      // 1) no animation action will block
      if (sAnimationHandler != null) {
        sAnimationHandler.removeAll(null);
      }

      // 2) clear and wait possibly
      if (waitForBlock) {
        sHandler.removeMessages(MSG_NORMAL);
      } else {
        sHandler.removeCallbacksAndMessages(null);
      }
      (sClosingWaiter = new Runnable() {
        @Override
        public void run() {
          if (!sHandler.hasMessages(MSG_BLOCK)) {
            sState = STATE_CLOSED;
            sHandler = null;
            sAnimationHandler = null;
            sClosingWaiter = null;
            if (done != null) {
              done.run();
            }
          } else {
            sHandler.postDelayed(this, 100);
          }
        }
      }).run();
    } else {
      Check.d(done == null);
    }
  }

  public static boolean isClosed() {
    return sState == STATE_CLOSED;
  }

  public static void runOnMainThread(Runnable r) {
    if (RuntimeInfoHelper.currentlyOnMainThread()) {
      r.run();
    } else {
      post(r);
    }
  }

  // Handler

  static Handler sHandler;

  public static Handler get() {
    return sHandler;
  }

  public static void post(Runnable r) {
    post(false, null, r);
  }

  public static void post(boolean block, Runnable r) {
    post(block, null, r);
  }

  public static void post(Object token, Runnable r) {
    post(false, token, r);
  }

  public static void post(boolean block, Object token, Runnable r) {
    if (sState == STATE_OPENED) {
      sHandler.sendMessage(getPostMessage(block, token, r));
    } else {
      Check.d(false);
    }
  }

  public static void postDelayed(Runnable r, long delayMillis) {
    postDelayed(false, null, r, delayMillis);
  }

  public static void postDelayed(Object token, Runnable r, long delayMillis) {
    postDelayed(false, token, r, delayMillis);
  }

  public static void postDelayed(boolean block, Runnable r, long delayMillis) {
    postDelayed(block, null, r, delayMillis);
  }

  public static void postDelayed(boolean block, Object token, Runnable r, long delayMillis) {
    if (sState == STATE_OPENED) {
      sHandler.sendMessageDelayed(getPostMessage(block, token, r), delayMillis);
    } else {
      Check.d(false);
    }
  }

  public static void postAtFront(Runnable r) {
    postAtFront(false, null, r);
  }

  public static void postAtFront(Object token, Runnable r) {
    postAtFront(false, token, r);
  }

  public static void postAtFront(boolean block, Runnable r) {
    postAtFront(block, null, r);
  }

  public static void postAtFront(boolean block, Object token, Runnable r) {
    if (sState == STATE_OPENED) {
      sHandler.sendMessageAtFrontOfQueue(getPostMessage(block, token, r));
    } else {
      Check.d(false);
    }
  }

  public static void remove(Runnable r) {
    remove(r, null);
  }

  public static void remove(Runnable r, Object token) {
    if (sState == STATE_OPENED) {
      sHandler.removeCallbacks(r, token);

      if (sAnimationHandler != null) {
        sAnimationHandler.remove(r, token);
      }
    } else {
      Check.d(false);
    }
  }

  public static void removeAll(Object token) {
    if (sState == STATE_OPENED) {
      sHandler.removeCallbacksAndMessages(token);

      if (sAnimationHandler != null) {
        sAnimationHandler.removeAll(token);
      }
    } else {
      Check.d(false);
    }
  }

  private static final int MSG_NORMAL = 0;
  private static final int MSG_BLOCK = 1;

  private static Message getPostMessage(boolean block, Object token, Runnable r) {
    Message m = Message.obtain(sHandler, r);
    m.what = block ? MSG_BLOCK : MSG_NORMAL;
    m.obj = token;
    return m;
  }

  // Animation Handler

  static AnimationHandler sAnimationHandler;

  public static void postOnAnimation(Runnable r) {
    postOnAnimation(null, r);
  }

  public static void postOnAnimation(Object token, Runnable r) {
    if (sState == STATE_OPENED) {
      if (sAnimationHandler != null) {
        sAnimationHandler.post(token, r);
      } else {
        postAtFront(token, r);
      }
    } else {
      Check.d(false);
    }
  }

  /**
   * Because we have no way to clear pending frames in {@link Choreographer},
   * we use only one {@link FrameCallback} to manage all runnables we
   * post.
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static class AnimationHandler implements FrameCallback {
    private final Choreographer mChoreographer = Choreographer.getInstance();
    private ArrayList<Object> mActiveList = new ArrayList<>();
    private ArrayList<Object> mNextList = new ArrayList<>();

    public void post(Object token, Runnable r) {
      ArrayList<Object> list = mActiveList;
      if (list.isEmpty()) {
        mChoreographer.postFrameCallback(this);
      }
      list.add(token);
      list.add(r);
    }

    public void remove(Runnable r, Object token) {
      // 1) check empty
      ArrayList<Object> list = mActiveList;
      if (list.isEmpty()) {
        return;
      }

      // 2) do remove
      for (int i = list.size() - 1; i >= 0; i -= 2) {
        if (list.get(i) == r && (token == null || list.get(i - 1) == token)) {
          list.remove(i);
          list.remove(i - 1);
        }
      }

      // 3) detach
      if (list.isEmpty()) {
        mChoreographer.removeFrameCallback(this);
      }
    }

    public void removeAll(Object token) {
      // 1) check empty
      ArrayList<Object> list = mActiveList;
      if (list.isEmpty()) {
        return;
      }

      // 2) do remove
      if (token == null) {
        list.clear();
      } else {
        for (int i = list.size() - 2; i >= 0; i -= 2) {
          if (list.get(i) == token) {
            list.remove(i + 1);
            list.remove(i);
          }
        }
      }

      // 3) detach
      if (list.isEmpty()) {
        mChoreographer.removeFrameCallback(this);
      }
    }

    @Override
    public void doFrame(long frameTimeNanos) {
      // 1) do swap
      ArrayList<Object> list = mActiveList;
      mActiveList = mNextList;
      mNextList = list;

      // 2) run
      for (int i = 1, size = list.size(); i < size; i += 2) {
        ((Runnable) list.get(i)).run();
      }
      list.clear();
    }
  }
}
