package com.home77.kake.business.user.service;

import com.google.gson.Gson;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.business.user.service.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author CJ
 */
public class UserService {
  public final static String ACTION_LOGIN = "login";
  public final static String ACTION_REGISTER = "register";
  public final static String ACTION_ERROR = "error";

  public final static String USERNAME = "username";
  public final static String PHONE = "phone";
  public final static String NICKNAME = "nickname";

  private final static String VALIDATE_URL;
  private final static String REGISTER_URL;
  private final static String QUERY_URL;
  private static final String CHECKCODE_URL;
  private static final String USER_URL;
  private static final String TAG = UserService.class.getSimpleName();

  static {
    String host = "control.home77.com";
    VALIDATE_URL = String.format("https://%s/account/v1/validate", host);
    REGISTER_URL = String.format("https://%s/account/v1/register", host);
    QUERY_URL = String.format("https://%s/account/v1/query", host);
    CHECKCODE_URL = String.format("http://%s/laravel-sms/verify-code", host);
    USER_URL = String.format("http://%s/api/v1/user", host);
  }

  private URLFetcher urlFetcher;

  private UserService() {
  }

  public void login(String phoneNumber, String password, final URLFetcher.Delegate callback) {
    TokenUpdater tokenUpdater = Instance.of(TokenUpdater.class);
    tokenUpdater.start(phoneNumber, password);
    tokenUpdater.getTokenAsync(new TokenUpdater.OnTokenCallback() {
      @Override
      public void onGetTokenSuccess(String token) {
        urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                               .url(USER_URL)
                               .addHeader("Authorization", "Bearer " + token);
        urlFetcher.start();
      }

      @Override
      public void onGetTokenFail() {
        callback.onComplete(null);
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
                           .postJson(new Gson().toJson(registerRequest));
    urlFetcher.start();
  }

  public void gainCheckCode(String phoneNumber, URLFetcher.Delegate callback) {
    try {
      JSONObject jsoObject = new JSONObject();
      jsoObject.put("mobile", phoneNumber);
      urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                             .url(CHECKCODE_URL)
                             .postJson(jsoObject.toString());
      urlFetcher.start();
    } catch (JSONException e) {
      callback.onComplete(urlFetcher);
    }
  }
}
