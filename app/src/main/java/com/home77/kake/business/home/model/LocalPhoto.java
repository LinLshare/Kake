package com.home77.kake.business.home.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author CJ
 */
public class LocalPhoto implements Parcelable {
  private boolean isTitle;
  private long id;
  private String name;
  private long size;
  private long date;
  private String path;
  private boolean isSelect;

  protected LocalPhoto(Parcel in) {
    isTitle = in.readByte() != 0;
    id = in.readLong();
    name = in.readString();
    size = in.readLong();
    date = in.readLong();
    path = in.readString();
    isSelect = in.readByte() != 0;
  }

  public static final Creator<LocalPhoto> CREATOR = new Creator<LocalPhoto>() {
    @Override
    public LocalPhoto createFromParcel(Parcel in) {
      return new LocalPhoto(in);
    }

    @Override
    public LocalPhoto[] newArray(int size) {
      return new LocalPhoto[size];
    }
  };

  public static LocalPhoto makeTitle(long date) {
    LocalPhoto localPhoto = new LocalPhoto();
    localPhoto.isTitle = true;
    localPhoto.date = date;
    return localPhoto;
  }

  private LocalPhoto() {
  }

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

  public boolean isTitle() {
    return isTitle;
  }

  public boolean isSelect() {
    return isSelect;
  }

  public void setSelect(boolean select) {
    isSelect = select;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (isTitle ? 1 : 0));
    dest.writeLong(id);
    dest.writeString(name);
    dest.writeLong(size);
    dest.writeLong(date);
    dest.writeString(path);
    dest.writeByte((byte) (isSelect ? 1 : 0));
  }
}
