package com.fish.live;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fish.live.home.MainActivity;
import com.fish.live.login.LoginActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.UiGoto;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.rl_bottom_layout)
    RelativeLayout rlBottomLayout;
    private Disposable mDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags", int.class);
            method.invoke(getWindow(), flag);
        } catch (Exception e) {
            Log.i("screen adpter", "addExtraFlags not found.");
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).keyboardEnable(true).init();
        getPermission();
    }

    @Override
    protected void initData() {
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        tvSkip.setText("跳过" + (3 - aLong));
                        if (aLong == 3) {
//                            if (!SharePreUtils.getjwt_token(mContext).equals("")) {
//                                UiGoto.startAty(mContext, MainActivity.class);
//                                //UiGoto.startAty(mContext, DBActivity.class);
//                                finish();
//                            } else {
                                UiGoto.startAty(mContext, LoginActivity.class);
                                finish();
//                            }
                        }
                    }
                });
        addSubscribe(mDisposable);
    }

    @SuppressLint("CheckResult")
    private void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO

        )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //用户同意使用权限
                        } else {
                            //用户不同意使用权限
                            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                            Toast.makeText(SplashActivity.this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @OnClick({R.id.iv_bg, R.id.tv_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bg:
//                EventBus.getDefault().postSticky(new MsgEvent("百度一下","https://www.baidu.com/",MsgEvent.TYPE_WEB));
                UiGoto.startAty(mContext, MainActivity.class);
                finish();
//                WebActivity.lauch(mContext, "百度一下", "www.baidu.com");
                break;
            case R.id.tv_skip:
                UiGoto.startAty(mContext, MainActivity.class);
                finish();
                break;
        }
    }

}
