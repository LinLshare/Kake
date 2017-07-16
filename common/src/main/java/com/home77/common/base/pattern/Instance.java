package com.home77.common.base.pattern;

import com.home77.common.BuildConfig;
import com.home77.common.base.debug.Check;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link Instance} supports global singleton patterns:
 * <p/>
 * <h1>Global Singleton</h1>
 * <p>
 * Directly invoke <code>Instance.of(X.class)</code> to get a gloabl singleton
 * instance, but the X class must own a non-public non-parametric constructor.
 * </p>
 */
public class Instance {
  private static final Map<Class<?>, Object> sInstanceMap;

  static {
    sInstanceMap = new ConcurrentHashMap<>();
  }

  @SuppressWarnings("unchecked")
  public static <T> T of(Class<T> cls) {
    if (cls == null) {
      return null;
    }

    // 1) get existed instance
    Object instance = sInstanceMap.get(cls);

    // 2) create instance automatically
    if (instance == null) {
      try {
        Constructor<T> ctr = cls.getDeclaredConstructor();

        if (BuildConfig.DEBUG_BUILD) {
          /**
           * Only a class without public default constructor can be
           * used to gen global instance automatically
           */
          Check.d(!Modifier.isPublic(ctr.getModifiers()));
        }

        ctr.setAccessible(true);
        sInstanceMap.put(cls, instance = ctr.newInstance());
      } catch (Throwable e) {
        Check.r(e);
      }
    }

    return (T) instance;
  }

  public static void destroy(Class<?> cls) {
    if (sInstanceMap.containsKey(cls)) {
      sInstanceMap.remove(cls);
    }
  }

  public static void reset() {
    sInstanceMap.clear();
  }
}
