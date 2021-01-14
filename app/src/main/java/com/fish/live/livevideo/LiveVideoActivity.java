package com.fish.live.livevideo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fish.live.Constants;
import com.fish.live.R;
import com.fish.live.livevideo.adapter.LivePagerAdapter;
import com.fish.live.livevideo.presenter.LiveVideoPresenter;
import com.fish.live.livevideo.view.LiveVideoCotract;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.widget.TitleLayout;
import com.nucarf.base.widget.ViewPagerSlide;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LiveVideoActivity extends BaseMvpActivity<LiveVideoPresenter> implements LiveVideoCotract.View, SuperPlayerView.OnSuperPlayerViewCallback {


    private  final String TAG = LiveVideoActivity.this.getClass().getSimpleName();
    @BindView(R.id.title_layout)
    TitleLayout titleLayout;
    @BindView(R.id.player_content)
    FrameLayout playerContent;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;
//    @BindView(R.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;
    private boolean mDefaultVideo;
    private int mVideoCount;
    private boolean mVideoHasPlay;

    @Override
    protected int getLayout() {
        return R.layout.activity_live_video;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String from = intent.getStringExtra("from");
        if (!TextUtils.isEmpty(from)) {
//            playExternalVideo();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    protected void initInject() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ImmersionBar.with(this).statusBarDarkFont(false, 0.2f).titleBar(tabLayout).init();
        titleLayout.setLeftClickListener((v) -> finish());
        titleLayout.setTitleText("直播详情");
        LivePagerAdapter livePagerAdapter = new LivePagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(livePagerAdapter);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("简介");
        strings.add("文档");
        strings.add("聊天");
        livePagerAdapter.setData(strings);
        vpMain.setOffscreenPageLimit(strings.size());
        tabLayout.setViewPager(vpMain, (String[]) strings.toArray(new String[strings.size()]));
        mSuperPlayerView = new SuperPlayerView(this);
        playerContent.addView(mSuperPlayerView,FrameLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.dp2px(mContext,200));
        mSuperPlayerView.setPlayerViewCallback(this);

    }

    @Override
    protected void initViewAndData() {
        mDefaultVideo = getIntent().getBooleanExtra(Constants.PLAYER_DEFAULT_VIDEO, true);

        initSuperVodGlobalSetting();
        mVideoHasPlay = false;
        mVideoCount = 0;

        TXLiveBase.setAppID("1253131631");//官方默认id
//        TXLiveBase.setAppID(Constants.VOD_APPID+"");//我的id
        mSuperPlayerView.play(Constants.NORMAL_PLAY_URL);
    }

    @Override
    public void setData(ArrayList<String> data) {

    }

    @Override
    public void getMoneySuccess() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PAUSE) {
            Log.i(TAG, "onResume state :" + mSuperPlayerView.getPlayerState());
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FLOAT) {
                mSuperPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
            }
        }
        if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            //隐藏虚拟按键，并且全屏
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                decorView.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause state :" + mSuperPlayerView.getPlayerState());
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.resetPlayer();
        }
//        VideoDataMgr.getInstance().setGetVideoInfoListListener(null);
    }

    @Override
    public void onStartFullScreenPlay() {
        // 隐藏其他元素实现全屏
        titleLayout.setVisibility(GONE);
//        if (mImageAdd != null) {
//            mImageAdd.setVisibility(GONE);
//        }
    }

    @Override
    public void onStopFullScreenPlay() {
        // 恢复原有元素
        titleLayout.setVisibility(VISIBLE);
//        if (mImageAdd != null) {
//            mImageAdd.setVisibility(VISIBLE);
//        }
    }

    @Override
    public void onClickFloatCloseBtn() {
        // 点击悬浮窗关闭按钮，那么结束整个播放
        mSuperPlayerView.resetPlayer();
        finish();
    }

    @Override
    public void onClickSmallReturnBtn() {
        // 点击小窗模式下返回按钮，开始悬浮播放
//        showFloatWindow();
    }

    @Override
    public void onStartFloatWindowPlay() {
        // 开始悬浮播放后，直接返回到桌面，进行悬浮播放
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 初始化超级播放器全局配置
     */
    private void initSuperVodGlobalSetting() {
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 810;
        rect.height = 540;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
    }

}
