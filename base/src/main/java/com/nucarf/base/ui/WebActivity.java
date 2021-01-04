package com.nucarf.base.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.nucarf.base.R;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivityWithTitle {

    WebView webView;
    private String url;

    public static void lauch(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        webView = findViewById(R.id.webView);
        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        titlelayout.setTitleText(title);
        initWeb();
    }

    @Override
    protected void initData() {

    }

    private void initWeb() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速

        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        webView.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAppCachePath(this.getDir("appcache", 0).getPath());
        webView.getSettings().setDatabasePath(this.getDir("databases", 0).getPath());
        webView.getSettings().setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(false);
        // 修改ua使得web端正确判断
//        String ua = webView.getSettings().getUserAgentString();
//        webView.getSettings().setUserAgentString(ua + ";isApp");
        // 设置允许JS弹窗
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        LogUtils.e("url--web--" + url);
        loadH5(url);
    }

    private void loadH5(final String url) {
        webView.addJavascriptInterface(new JsEventInterface(), "$api");
        webView.loadUrl(url);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                // url 你要访问的下载链接
                // userAgent 是HTTP请求头部用来标识客户端信息的字符串
                // contentDisposition 为保存文件提供一个默认的文件名
                // mimetype 该资源的媒体类型
                // contentLength 该资源的大小
                // 这几个参数都是可以通过抓包获取的
                // 用手机默认浏览器打开链接
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        // 与js交互
        webView.setWebViewClient(new WebViewClient() {
            //webview加载https链接错误或无响应
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (handler != null) {
                    handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                String key = "token";
//                String value = SharePreUtils.getjwt_token(mContext);
//                webView.loadUrl("javascript:window.localStorage.setItem('" + key + "','" + value + "');" +
//                        "javascript:window.localStorage.setItem('platform','Android');");
//                webView.loadUrl("javascript:window.localStorage.setItem('isAppWebview','1');");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
    }

    class JsEventInterface {

        public JsEventInterface() {

        }

        @JavascriptInterface
        public String h5_call_function_token() {
            return SharePreUtils.getjwt_token(WebActivity.this);
        }

        @JavascriptInterface
        public void close() {
            finish();
        }

        @JavascriptInterface
        public void getLocation(String fucName) {
            String js = "javascript:" + fucName + "(1," + "{lat:39.910331,lng:116.470041}" + ")";
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(js);
                }
            });
        }


        @JavascriptInterface
        public void h5_call_function_jumpapp(String action) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()， 再 destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading(); // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

}
