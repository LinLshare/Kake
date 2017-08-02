package com.home77.kake.business.theta;

import android.graphics.Bitmap;

import com.theta360.v2.model.ImageSize;
import com.theta360.v2.network.DeviceInfo;
import com.theta360.v2.network.HttpConnector;
import com.theta360.v2.network.HttpDownloadListener;
import com.theta360.v2.network.HttpEventListener;
import com.theta360.v2.network.ImageData;
import com.theta360.v2.network.ImageInfo;
import com.theta360.v2.network.StorageInfo;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author CJ
 */
public class ThetaCameraApiImpl implements ThetaCameraApi {
  private HttpConnector httpConnector;

  public ThetaCameraApiImpl() {
    httpConnector = new HttpConnector(CAMERA_IP_ADDRESS);
  }

  @Override
  public StorageInfo getStorageInfo() {
    return httpConnector.getStorageInfo();
  }

  @Override
  public DeviceInfo getDeviceInfo() {
    return httpConnector.getDeviceInfo();
  }

  @Override
  public ArrayList<ImageInfo> getList() {
    return httpConnector.getList();
  }

  @Override
  public Bitmap getThumb(String fileId) {
    return httpConnector.getThumb(fileId);
  }

  @Override
  public HttpConnector.ShootResult takePicture(HttpEventListener listener) {
    return httpConnector.takePicture(listener);
  }

  @Override
  public ImageData getImage(String fileId, HttpDownloadListener listener) {
    return httpConnector.getImage(fileId, listener);
  }

  @Override
  public InputStream getLivePreview() throws IOException, JSONException {
    return httpConnector.getLivePreview();
  }

  @Override
  public void deleteFile(String deletedFileId, HttpEventListener listener) {
    httpConnector.deleteFile(deletedFileId, listener);
  }

  @Override
  public void setImageSize(ImageSize imageSize) {
    httpConnector.setImageSize(imageSize);
  }

  @Override
  public ImageSize getImageSize() {
    return httpConnector.getImageSize();
  }
}
