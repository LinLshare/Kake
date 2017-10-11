package com.home77.kake.business.home;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.home77.common.base.debug.DLog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.R;
import com.home77.kake.common.utils.Util;
import com.home77.kake.common.widget.CustomBottomDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinoWebPageActivity extends AppCompatActivity {

  private static final String TAG = PinoWebPageActivity.class.getSimpleName();
  @BindView(R.id.web_view)
  WebView webView;
  public static final String EXTRA_PANO_NAME = "pano_name";
  public static final String EXTRA_PANO_URL = "pano_url";

  private String name;
  private String url;
  private IWXAPI api;

  public static void start(Context context, String name, String url) {
    Intent intent = new Intent(context, PinoWebPageActivity.class);
    intent.putExtra(EXTRA_PANO_NAME, name);
    intent.putExtra(EXTRA_PANO_URL, url);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
    setContentView(R.layout.activity_web_page);
    ButterKnife.bind(this);

    this.name = getIntent().getStringExtra(EXTRA_PANO_NAME);
    this.url = getIntent().getStringExtra(EXTRA_PANO_URL);
    String appId = "wx0dfd1d91a80a49b1";
    api = WXAPIFactory.createWXAPI(this, appId, true);

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
    webView.loadUrl(url);
    //    webView.loadDataWithBaseURL(url + "", "", "text/html", "utf-8", null);
  }

  @OnClick({R.id.back_image_view, R.id.share_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        this.finish();
        break;
      case R.id.share_image_view:
        showShareDialog();
        break;
    }
  }

  private void showShareDialog() {
    View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
    final Dialog alertDialog = new CustomBottomDialog(this);
    alertDialog.getWindow().setWindowAnimations(R.style.dialogStyle);
    alertDialog.setContentView(dialogView);
    dialogView.findViewById(R.id.cancel_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.friend_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        shareWetChat(SendMessageToWX.Req.WXSceneTimeline);
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.moment_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        shareWetChat(SendMessageToWX.Req.WXSceneSession);
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.copy_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ClipboardManager clipboardManager =
            (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(url + "");
        Toast.showShort("已经复制到剪贴板");
        alertDialog.dismiss();
      }
    });
    alertDialog.show();
  }

  private void shareWetChat(final int scene) {
    Picasso.with(this).load(url).resize(400, 400).centerCrop().into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;

        WXMediaMessage wxMsg = new WXMediaMessage(webpageObject);
        wxMsg.title = name;
        wxMsg.description = "";
        wxMsg.thumbData = Util.bmpToByteArray(bitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");

        req.message = wxMsg;
        req.scene = scene;

        api.sendReq(req);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    });
  }

  private String buildTransaction(final String type) {
    return (type == null)
        ? String.valueOf(System.currentTimeMillis())
        : type + System.currentTimeMillis();
  }
}
