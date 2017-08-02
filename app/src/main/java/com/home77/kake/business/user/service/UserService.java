package com.home77.kake.business.user.service;

import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.business.user.service.request.RegisterRequest;
import com.home77.kake.business.user.service.request.TokenValidateRequest;
import com.home77.kake.business.user.service.response.TokenValidateResponse;

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
  private static final String TAG = UserService.class.getSimpleName();

  public final static String NICKNAME = "nickname";
  private final static String REGISTER_URL;
  private final static String QUERY_URL;
  private static final String CHECKCODE_URL;
  private static final String USER_URL;
  private static final String VALIDATE_URL;

  static {
    String host = "control.home77.com";
    VALIDATE_URL = String.format("http://%s/oauth/token", host);
    REGISTER_URL = String.format("https://%s/account/v1/register", host);
    QUERY_URL = String.format("https://%s/account/v1/query", host);
    CHECKCODE_URL = String.format("http://%s/laravel-sms/verify-code", host);
    USER_URL = String.format("http://%s/api/v1/user", host);
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
          urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                                 .url(USER_URL)
                                 .addHeader("Authorization",
                                            "Bearer " + tokenValidateResponse.getAccess_token());
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

  public void gainCheckCode(String phoneNumber, URLFetcher.Delegate callback) {
    try {
      JSONObject jsoObject = new JSONObject();
      jsoObject.put("mobile", phoneNumber);
      urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                             .url(CHECKCODE_URL)
                             .postJson(jsoObject);
      urlFetcher.start();
    } catch (JSONException e) {
      callback.onError(e.getMessage());
    }
  }
}
