package com.home77.common.protocol.base;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.home77.common.protocol.Json;

import java.lang.reflect.Type;

public class BasicRequest<T> {

  // static

  public static <T> BasicRequest<T> fromJson(String json, Class<T> clazz) {
    final Type type = new TypeToken<BasicRequest<JsonObject>>() {
    }.getType();
    final BasicRequest<JsonObject> result = Json.fromJson(json, type);
    final T payload = Json.fromJson(result.data(), clazz);
    return BasicRequest.make(result.info(), result.token(), payload);
  }

  public static <T> BasicRequest<T> make(Info info, Token token, T data) {
    return new BasicRequest<>(info, token, data);
  }

  // class

  private BasicRequest(Info info, Token token, T data) {
    mInfo = info;
    mToken = token;
    mData = data;
  }

  @SerializedName("info")
  private Info mInfo;

  @SerializedName("token")
  private Token mToken;

  @SerializedName("data")
  private T mData;

  // getter

  public Info info() {
    return mInfo;
  }

  public Token token() {
    return mToken;
  }

  public T data() {
    return mData;
  }
}
