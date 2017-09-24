package com.home77.kake.business.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.home77.common.base.debug.DLog;
import com.home77.kake.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPageActivity extends AppCompatActivity {

  private static final String TAG = WebPageActivity.class.getSimpleName();
  @BindView(R.id.title_text_view)
  TextView titleTextView;
  @BindView(R.id.web_view)
  WebView webView;
  public static final String EXTRA_PANO_NAME = "pano_name";
  public static final String EXTRA_PANO_URL = "pano_url";

  private String name;
  private String url;

  public static void start(Context context, String name, String url) {
    Intent intent = new Intent(context, WebPageActivity.class);
    intent.putExtra(EXTRA_PANO_NAME, name);
    intent.putExtra(EXTRA_PANO_URL, url);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web_page);
    ButterKnife.bind(this);

    this.name = getIntent().getStringExtra(EXTRA_PANO_NAME);
    this.url = getIntent().getStringExtra(EXTRA_PANO_URL);
    titleTextView.setText(name + "");

    //声明WebSettings子类
    WebSettings webSettings = webView.getSettings();

    //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
    webSettings.setJavaScriptEnabled(true);
    // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
    // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
    //设置自适应屏幕，两者合用
    webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
    webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

    //缩放操作
    webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
    webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
    webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

    //其他细节操作
    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
    webSettings.setAllowFileAccess(true); //设置可以访问文件
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
    webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
    webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }
    });
    DLog.d(TAG, "url: " + url);
    webView.loadDataWithBaseURL(url + "", "", "text/html", "utf-8", null);
  }

  @OnClick(R.id.back_image_view)
  public void onViewClicked() {
    this.finish();
  }
}
