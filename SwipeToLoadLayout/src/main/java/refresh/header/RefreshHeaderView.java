package refresh.header;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import refresh.swipe.SwipeRefreshHeaderLayout;
import sun.bo.lin.refresh.R;

public class RefreshHeaderView extends SwipeRefreshHeaderLayout {
    private ImageView refresh_iv;
    private ProgressBar mProBar;
    private int mHeaderHeight;


    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh_iv = (ImageView) findViewById(R.id.refresh_iv);
        mProBar = (ProgressBar) findViewById(R.id.pro_bar);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onRefresh() {
        refresh_iv.setVisibility(GONE);
        mProBar.setVisibility(VISIBLE);
    }

    @Override
    public void onPrepare() {
        refresh_iv.setVisibility(GONE);
        mProBar.setVisibility(VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        refresh_iv.setVisibility(VISIBLE);
        mProBar.setVisibility(GONE);
        if (y <= mHeaderHeight) {

        }
    }

    @Override
    public void onRelease() {
        refresh_iv.setVisibility(GONE);
        mProBar.setVisibility(VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onComplete() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReset() {
        refresh_iv.setVisibility(GONE);
        mProBar.setVisibility(VISIBLE);
    }
}
