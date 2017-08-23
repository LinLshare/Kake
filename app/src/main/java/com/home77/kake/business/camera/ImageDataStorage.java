package com.home77.kake.business.camera;

import com.home77.common.base.data.StorageData;

/**
 * @author CJ
 */
public class ImageDataStorage extends StorageData {
  private static final String KEY_YAW = "yaw";
  private static final String KEY_PITCH = "pitch";
  private static final String KEY_ROLL = "roll";

  private ImageDataStorage() {
    super("img_meta_data");
  }

  public ImageDataStorage putYaw(Double yaw) {
    put(KEY_YAW, String.valueOf(yaw));
    return this;
  }

  public Double getYaw() {
    return Double.valueOf(getString(KEY_YAW, "0.00"));
  }

  public ImageDataStorage putPitch(Double pitch) {
    put(KEY_PITCH, String.valueOf(pitch));
    return this;
  }

  public Double getPitch() {
    return Double.valueOf(getString(KEY_PITCH, "0.00"));
  }

  public ImageDataStorage putRoll(Double roll) {
    put(KEY_ROLL, String.valueOf(roll));
    return this;
  }

  public Double getRoll() {
    return Double.valueOf(getString(KEY_ROLL, "0.00"));
  }

}
