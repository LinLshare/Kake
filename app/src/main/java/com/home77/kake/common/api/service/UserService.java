package com.home77.kake.common.api.service;

import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.common.api.request.CheckCodeRequest;
import com.home77.kake.common.api.request.RegisterRequest;
import com.home77.kake.common.api.request.TokenValidateRequest;
import com.home77.kake.common.api.request.UpdateUserNameRequest;
import com.home77.kake.common.api.response.TokenValidateResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.home77.kake.common.api.ServerConfig.HOST;

/**
 * @author CJ
 */
public class UserService {

  private final static String REGISTER_URL;
  private static final String CHECKCODE_URL;
  private static final String USER_URL;
  private static final String VALIDATE_URL;
  private static final String RESET_PASSWORD_URL;
  private static final String UPDATE_USER_NAME_URL;
  private static final String UPDATE_AVATAR_URL;

  static {
    VALIDATE_URL = String.format("http://%s/oauth/token", HOST);
    REGISTER_URL = String.format("http://%s/api/v1/register", HOST);
    CHECKCODE_URL = String.format("http://%s/laravel-sms/verify-code", HOST);
    USER_URL = String.format("http://%s/api/v1/user", HOST);
    RESET_PASSWORD_URL = String.format("http://%s/api/v1/resetpassword", HOST);
    UPDATE_USER_NAME_URL = String.format("http://%s/api/v1/user/changename", HOST);
    UPDATE_AVATAR_URL = String.format("http://%s/api/v1/user/changephoto", HOST);
  }

  private URLFetcher urlFetcher;

  private UserService() {
  }

  private static final String CLIENT_SECRET = "V59K95xzfdhjNlcq3TZaW9KWJwufgp6w8RH8uFU4";
  private static final int CLIENT_ID = 3;
  private static final String GRANT_TYPE = "password";
  private static final String SCOPE = "";

  public void token(String userName, String password, final URLFetcher.Delegate callback) {
    TokenValidateRequest request =
        new TokenValidateRequest(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, userName, password, SCOPE);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(VALIDATE_URL)
                           .postJson(request);
    urlFetcher.start();
  }

  public void login(String phoneNumber, String password, final URLFetcher.Delegate callback) {
    token(phoneNumber, password, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        TokenValidateResponse tokenValidateResponse =
            source.responseClass(TokenValidateResponse.class);
        if (tokenValidateResponse != null) {
          App.globalData()
             .putString(GlobalData.KEY_ACCESS_TOKEN, tokenValidateResponse.getAccess_token())
             .putString(GlobalData.KEY_REFRESH_TOKEN, tokenValidateResponse.getRefresh_token())
             .putInt(GlobalData.KEY_EXPIRE_IN, tokenValidateResponse.getExpires_in())
             .putString(GlobalData.KEY_TOKEN_TYPE, tokenValidateResponse.getAccess_token());

          urlFetcher = createUrlFetcher(callback).url(USER_URL);
          urlFetcher.start();
        } else {
          callback.onError("parse response error");
        }
      }

      @Override
      public void onError(String msg) {
        callback.onError(msg);
      }
    });
  }

  public void register(String phoneNumber,
                       String checkCode,
                       String password,
                       String confirmPassword,
                       URLFetcher.Delegate callback) {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setMobile(phoneNumber);
    registerRequest.setCode(checkCode);
    registerRequest.setPassword(password);
    registerRequest.setRepassword(confirmPassword);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(REGISTER_URL)
                           .postJson(registerRequest);
    urlFetcher.start();
  }

  public void updateUserName(int userId, String newName, URLFetcher.Delegate callback) {
    UpdateUserNameRequest registerRequest = new UpdateUserNameRequest(userId, newName);
    urlFetcher = createUrlFetcher(callback).url(UPDATE_USER_NAME_URL).postJson(registerRequest);
    urlFetcher.start();
  }

  public void updateAvater(File file, URLFetcher.Delegate callback) {
    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                         .addFormDataPart("file",
                                                                          "head_image",
                                                                          fileBody)
                                                         .build();
    urlFetcher = createUrlFetcher(callback).url(UPDATE_AVATAR_URL).postRequestBody(requestBody);
    urlFetcher.start();
  }


  public void resetPassword(String phoneNumber,
                            String checkCode,
                            String password,
                            URLFetcher.Delegate callback) {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setMobile(phoneNumber);
    registerRequest.setCode(checkCode);
    registerRequest.setRepassword(password);
    registerRequest.setPassword(password);
    urlFetcher = createUrlFetcher(callback).url(RESET_PASSWORD_URL).postJson(registerRequest);
    urlFetcher.start();
  }

  public void gainCheckCode(String phoneNumber, URLFetcher.Delegate callback) {
    urlFetcher =
        createUrlFetcher(callback).url(CHECKCODE_URL).postJson(new CheckCodeRequest(phoneNumber));
    urlFetcher.start();
  }

  private URLFetcher createUrlFetcher(URLFetcher.Delegate callback) {
    return URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                     .addHeader("Authorization",
                                "Bearer " +
                                App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
  }
}
