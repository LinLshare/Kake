package com.home77.kake.business.theta;

import com.home77.kake.base.Mapper;
import com.home77.kake.business.home.model.Photo;

import java.util.List;

/**
 * @author CJ
 */
public class LoadFileListResult {

  /**
   * entries : [{"name":"R0010017.JPG","fileUrl":"http://192.168.1.1/files/abcde/100RICOH/R0010017.JPG","size":4051440,"dateTimeZone":"2015:07:10
   * 11:05:18+09:00","lat":50.5324,"lng":-120.2332,"width":5376,"height":2688,"isProcessed":true,"previewUrl":"","thumbnail":"(base64_binary)","_thumbSize":3348}]
   * totalEntries : 16
   */

  private int totalEntries;
  private List<EntriesBean> entries;
  private String continuationToken;

  public int getTotalEntries() {
    return totalEntries;
  }

  public void setTotalEntries(int totalEntries) {
    this.totalEntries = totalEntries;
  }

  public List<EntriesBean> getEntries() {
    return entries;
  }

  public void setEntries(List<EntriesBean> entries) {
    this.entries = entries;
  }

  public String getContinuationToken() {
    return continuationToken;
  }

  public void setContinuationToken(String continuationToken) {
    this.continuationToken = continuationToken;
  }

  public static class EntriesBean implements Mapper<Photo> {
    /**
     * name : R0010017.JPG
     * fileUrl : http://192.168.1.1/files/abcde/100RICOH/R0010017.JPG
     * size : 4051440
     * dateTimeZone : 2015:07:10 11:05:18+09:00
     * lat : 50.5324
     * lng : -120.2332
     * width : 5376
     * height : 2688
     * isProcessed : true
     * previewUrl :
     * thumbnail : (base64_binary)
     * _thumbSize : 3348
     */

    private String name;
    private String fileUrl;
    private int size;
    private String dateTimeZone;
    private double lat;
    private double lng;
    private int width;
    private int height;
    private boolean isProcessed;
    private String previewUrl;
    private String thumbnail;
    private int _thumbSize;

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

    public double getLat() {
      return lat;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }

    public double getLng() {
      return lng;
    }

    public void setLng(double lng) {
      this.lng = lng;
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

    public boolean isIsProcessed() {
      return isProcessed;
    }

    public void setIsProcessed(boolean isProcessed) {
      this.isProcessed = isProcessed;
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

    public int get_thumbSize() {
      return _thumbSize;
    }

    public void set_thumbSize(int _thumbSize) {
      this._thumbSize = _thumbSize;
    }

    @Override
    public Photo map() {
      Photo photo = new Photo();
      photo.setDateTimeZone(dateTimeZone);
      photo.setFileUrl(fileUrl);
      photo.setHeight(height);
      photo.setWidth(width);
      photo.setName(name);
      photo.setSize(size);
      photo.setPreviewUrl(previewUrl);
      photo.setThumbnail(thumbnail);
      return photo;
    }
  }
}
