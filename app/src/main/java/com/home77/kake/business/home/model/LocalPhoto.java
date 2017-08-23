package com.home77.kake.business.home.model;

import java.io.Serializable;

/**
 * @author CJ
 */
public class LocalPhoto implements Serializable {
  private long id;
  private String name;
  private long size;
  private long date;
  private String path;

  public LocalPhoto(long id, String name, long size, long date, String path) {
    this.id = id;
    this.name = name;
    this.size = size;
    this.date = date;
    this.path = path;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public long getSize() {
    return size;
  }

  public long getDate() {
    return date;
  }

  public String getPath() {
    return path;
  }
}
