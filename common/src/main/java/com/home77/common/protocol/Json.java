package com.home77.common.protocol;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class Json {
  private final static class SerializedNameOnlyStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
      return f.getAnnotation(SerializedName.class) == null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }
  }

  private final static Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
                                                    .setExclusionStrategies(new SerializedNameOnlyStrategy())
                                                    .create();

  public static String toJson(Object object) {
    return GSON.toJson(object);
  }

  public static <T> T fromJson(String json, Class<T> cls) {
    return GSON.fromJson(json, cls);
  }

  public static <T> T fromJson(String json, Type type) {
    return GSON.fromJson(json, type);
  }

  public static <T> T fromJson(JsonElement json, Class<T> cls) {
    return GSON.fromJson(json, cls);
  }

  public static <T> List<T> fromJsonToList(String json, Class<T> cls) {
    return GSON.fromJson(json, new ListOfJson<>(cls));
  }

  public static <T> List<T> fromJsonToList(JsonElement json, Class<T> cls) {
    return GSON.fromJson(json, new ListOfJson<>(cls));
  }

  // List type wrapper

  public static class ListOfJson<T> implements ParameterizedType {
    public ListOfJson(Class<T> type) {
      mWrappedType = type;
    }

    private final Class<?> mWrappedType;

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[] {mWrappedType};
    }

    @Override
    public Type getRawType() {
      return List.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }
  }
}
