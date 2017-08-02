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
public interface ThetaCameraApi {
  String CAMERA_IP_ADDRESS = "192.168.1.1";

  StorageInfo getStorageInfo();

  DeviceInfo getDeviceInfo();

  ArrayList<ImageInfo> getList();

  Bitmap getThumb(String fileId);

  HttpConnector.ShootResult takePicture(HttpEventListener listener);

  ImageData getImage(String fileId, HttpDownloadListener listener);


  InputStream getLivePreview() throws IOException, JSONException;

  void deleteFile(String deletedFileId, HttpEventListener listener);

  void setImageSize(ImageSize imageSize);

  ImageSize getImageSize();
}
