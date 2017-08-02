package com.home77.kake.business.theta;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author CJ
 */
public class ThetaRequest implements Serializable {

  public static final String FILE_TYPE_ALL = "all";
  public static final String FILE_TYPE_IMAGE = "image";
  public static final String FILE_TYPE_VIDEO = "video";

  private String name;
  private HashMap<String, Object> parameters;

  public ThetaRequest(String name) {
    this.name = name;
    parameters = new HashMap<>();
  }

  public static ThetaRequest create(String name) {
    return new ThetaRequest(name);
  }

  public ThetaRequest putParameter(String key, long value) throws JSONException {
    parameters.put(key, value);
    return this;
  }

  public ThetaRequest putParameter(String key, String value) throws JSONException {
    parameters.put(key, value);
    return this;
  }

  public ThetaRequest putParameter(String key, boolean value) throws JSONException {
    parameters.put(key, value);
    return this;
  }

  public ThetaRequest putParameter(String key, double value) throws JSONException {
    parameters.put(key, value);
    return this;
  }

  public ThetaRequest putParameter(String key, Object value) throws JSONException {
    parameters.put(key, value);
    return this;
  }

  public String getName() {
    return name;
  }
}
