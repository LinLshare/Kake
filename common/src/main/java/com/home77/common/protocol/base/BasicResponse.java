package com.home77.common.protocol.base;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.home77.common.protocol.ErrorCode;
import com.home77.common.protocol.Json;

import java.lang.reflect.Type;
import java.util.List;

import static com.home77.common.protocol.ErrorCode.SUCCESS;

public class BasicResponse<T> {
  // static

  private static final Type BASIC_RESPONSE_JSON_ELEMENT_TYPE =
      new TypeToken<BasicResponse<JsonElement>>() {
      }.getType();

  public static <D> BasicResponse<D> makeSuccess(D d) {
    return new BasicResponse<D>(SUCCESS, "success").withData(d);
  }

  public static <D extends AbstractEntry> BasicResponse<D> make(D d) {
    return new BasicResponse<D>(d.code(), d.msg()).withData(d);
  }

  public static <D> BasicResponse<D> make(int code, String msg, D d) {
    return new BasicResponse<D>(code, msg).withData(d);
  }

  public static BasicResponse make(int code, String msg) {
    return new BasicResponse(code, msg);
  }

  public static <T> BasicResponse<T> fromJsonObject(String json, Class<T> clazz) {
    // ). parse |BasicResponse|
    final BasicResponse<JsonElement> result = Json.fromJson(json, BASIC_RESPONSE_JSON_ELEMENT_TYPE);

    // ). parse |data| section
    BasicResponse<T> response;
    try {
      final T payload = Json.fromJson(result.data(), clazz);
      response = BasicResponse.make(result.code(), result.msg(), payload);
    } catch (Exception ignore) {
      response = BasicResponse.make(ErrorCode.SERVER_INTERNAL_ERROR,
                                    "parse json error ->" + ignore.getMessage(),
                                    null);
    }
    return response;
  }

  public static <T> BasicResponse<List<T>> fromJsonArray(String json, Class<T> clazz) {
    // ). parse |BasicResponse|
    final BasicResponse<JsonElement> result = Json.fromJson(json, BASIC_RESPONSE_JSON_ELEMENT_TYPE);

    // ). parse |data| section
    BasicResponse<List<T>> response;
    try {
      final List<T> payload = Json.fromJsonToList(result.data(), clazz);
      response = BasicResponse.make(result.code(), result.msg(), payload);
    } catch (Exception ignore) {
      response = BasicResponse.make(ErrorCode.SERVER_INTERNAL_ERROR,
                                    "parse json error ->" + ignore.getMessage(),
                                    null);
    }
    return response;
  }

  // class

  @SerializedName("code")
  private final int mCode;

  @SerializedName("msg")
  private final String mMsg;

  @SerializedName("data")
  private T mData;

  private BasicResponse(int code, String msg) {
    mCode = code;
    mMsg = msg;
  }

  private BasicResponse<T> withData(T data) {
    this.mData = data;
    return this;
  }

  public int code() {
    return mCode;
  }

  public String msg() {
    return mMsg;
  }

  public T data() {
    return mData;
  }
}


