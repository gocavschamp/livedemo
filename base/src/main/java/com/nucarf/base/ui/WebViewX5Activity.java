package com.nucarf.base.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.R;
import com.nucarf.base.R2;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.UiGoto;
//import com.tencent.smtt.export.external.interfaces.SslError;
//import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
//import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
//import com.tencent.smtt.sdk.DownloadListener;
//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebView;
//import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebViewX5Activity extends BaseActivity {

    WebView webView;
    @BindView(R2.id.iv_left)
    ImageView ivLeft;
    @BindView(R2.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R2.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R2.id.tv_right)
    TextView tvRight;
    @BindView(R2.id.iv_right)
    ImageView ivRight;
    @BindView(R2.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R2.id.rl_default_title_layout)
    RelativeLayout rlDefaultTitleLayout;
    @BindView(R2.id.rl_content)
    FrameLayout rlContent;
    private String url = "";
    private String type;

    public static void lauch(Context context, String title, String url, String type) {
        Intent intent = new Intent(context, WebViewX5Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webx5_view);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).titleBar(rlDefaultTitleLayout).init();

        tvCenterTitle.setTextColor(getResources().getColor(R.color.color_333333));
        rlDefaultTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        if ("guide".equals(type)) {
            rlDefaultTitleLayout.setVisibility(View.GONE);
        } else {
            rlDefaultTitleLayout.setVisibility(View.VISIBLE);

        }
        tvCenterTitle.setText(title);
        LogUtils.e("url--web--" + url);
        loadH5(url);
    }

    @Override
    protected void initData() {
        webView = new WebView(getApplicationContext());
        rlContent.addView(webView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        if ("guide".equals(type)) {
            rlDefaultTitleLayout.setVisibility(View.GONE);
        } else {
            rlDefaultTitleLayout.setVisibility(View.VISIBLE);

        }
        tvCenterTitle.setText(title);
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
        //设定缩放控件隐藏
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(false);
        // 修改ua使得web端正确判断
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + ";isApp");
        // 设置允许JS弹窗
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        // android 5.0以上默认不支持Mixed Content
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        LogUtils.e("url--web--" + url);
        loadH5(url);

    }

    private void loadH5(final String url) {
        webView.addJavascriptInterface(new ShareClickInterface(), "api");
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
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError sslError) {
                if (handler != null) {
                    handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            /**
             * 在开始加载网页时会回调
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isDialogShowing()) {
                    showDialog();
                }
            }

            /**
             * 在结束加载网页时会回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissDialog();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    dismissDialog();
//                } else {
//                    if (!isDialogShowing()) {
//                        showDialog();
//                    }
//                }
                super.onProgressChanged(view, newProgress);
            }
        });
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
        if (isDialogShowing()) {
            dismissDialog();
        }
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

    class ShareClickInterface {

        public ShareClickInterface() {

        }

        @JavascriptInterface
        public void h5_call_function_share(String shareInfo) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @JavascriptInterface
        public String h5_call_function_token() {
            return SharePreUtils.getjwt_token(mContext);
        }

        @JavascriptInterface
        public void close() {
            finish();
        }

        @JavascriptInterface
        public void getLocation(String fucName) {
            LogUtils.e("getLocation---" + fucName);
            String js = "javascript:" + fucName + "(1," + "'{lat:39.910331,lng:116.470041}'" + ")";
            LogUtils.e("getLocation--js-==" + js);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(js);
                }
            });
        }


        @JavascriptInterface
        public void h5_call_function_jumpapp(String action) {
            UiGoto.startAtyASAction(mContext, action);
        }

    }

    @OnClick(R2.id.rl_left)
    public void onViewClicked() {
        finish();
    }
}
