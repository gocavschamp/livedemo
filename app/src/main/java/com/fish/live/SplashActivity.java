package com.fish.live;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fish.live.home.MainActivity;
import com.fish.live.login.LoginActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.UiGoto;

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
                            if (!SharePreUtils.getjwt_token(mContext).equals("")) {
                                UiGoto.startAty(mContext, MainActivity.class);
                                //UiGoto.startAty(mContext, DBActivity.class);
                                finish();
                            } else {
                                UiGoto.startAty(mContext, LoginActivity.class);
                                finish();
                            }
                        }
                    }
                });
        addSubscribe(mDisposable);
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
