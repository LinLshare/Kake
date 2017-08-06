package com.home77.kake.business.home.model;

/**
 * @author CJ
 */
public class Photo {
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_GROUP = 1;
  private int type = TYPE_NORMAL;
  private String size;
  private String name;
  private String dateTimeZone;
  private byte[] thumbnail;

  public Photo(int type, String size, String name, String dateTimeZone, byte[] thumbnail) {
    this.type = type;
    this.size = size;
    this.name = name;
    this.dateTimeZone = dateTimeZone;
    this.thumbnail = thumbnail;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getDateTimeZone() {
    return dateTimeZone;
  }

  public void setDateTimeZone(String dateTimeZone) {
    this.dateTimeZone = dateTimeZone;
  }

  public byte[] getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(byte[] thumbnail) {
    this.thumbnail = thumbnail;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }
}
