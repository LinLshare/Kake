package com.home77.kake.business.home.model;

/**
 * @author CJ
 */
public class Photo {
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_GROUP = 1;
  private int type = TYPE_NORMAL;
  private String name;
  private String fileUrl;
  private int size;
  private String dateTimeZone;
  private int width;
  private int height;
  private String previewUrl;
  private String thumbnail;

  public static int getTypeNormal() {
    return TYPE_NORMAL;
  }

  public static int getTypeGroup() {
    return TYPE_GROUP;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getDateTimeZone() {
    return dateTimeZone;
  }

  public void setDateTimeZone(String dateTimeZone) {
    this.dateTimeZone = dateTimeZone;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getPreviewUrl() {
    return previewUrl;
  }

  public void setPreviewUrl(String previewUrl) {
    this.previewUrl = previewUrl;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }
}
