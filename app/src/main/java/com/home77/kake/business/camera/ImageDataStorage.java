package com.home77.kake.business.camera;

import com.home77.common.base.data.StorageData;

/**
 * @author CJ
 */
public class ImageDataStorage extends StorageData {
  private static final String KEY_YAW = "yaw";
  private static final String KEY_PITCH = "pitch";
  private static final String KEY_ROLL = "roll";

  private String imageName;

  private ImageDataStorage() {
    super("img_meta_data");
  }

  public ImageDataStorage bind(String imageName) {
    this.imageName = imageName;
    return this;
  }

  public ImageDataStorage putYaw(Double yaw) {
    put(imageName + KEY_YAW, String.valueOf(yaw));
    return this;
  }

  public Double getYaw() {
    return Double.valueOf(getString(imageName + KEY_YAW, "0.00"));
  }

  public ImageDataStorage putPitch(Double pitch) {
    put(imageName + KEY_PITCH, String.valueOf(pitch));
    return this;
  }

  public Double getPitch() {
    return Double.valueOf(getString(imageName + KEY_PITCH, "0.00"));
  }

  public ImageDataStorage putRoll(Double roll) {
    put(imageName + KEY_ROLL, String.valueOf(roll));
    return this;
  }

  public Double getRoll() {
    return Double.valueOf(getString(imageName + KEY_ROLL, "0.00"));
  }

}
