package com.nucarf.base.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.nucarf.base.R;
import com.nucarf.base.retrofit.CommonSubscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created tt on 16/6/6.
 */
public class PeriscopeLayout extends RelativeLayout {

    private Interpolator line = new LinearInterpolator();
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dce = new DecelerateInterpolator();
    private Interpolator accdec = new AccelerateDecelerateInterpolator();
    private Interpolator[] interpolators;

    private int mHeight;
    private int mWidth;
    private LayoutParams lp;
    private int[] drawables;
    private Random random = new Random();
    private int dHeight;
    private int dWidth;
    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;
    private int mGravity;
    private float mHeartMarginLeft;
    private float mHeartMarginRight;
    private CompositeDisposable subscriptions;
    private boolean isStar = false;
    private Disposable subscription;


    public PeriscopeLayout(Context context) {
        super(context);
    }

    public PeriscopeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PeriscopeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PeriscopeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void start() {
        if(isStar) {
            return;
        }
        isStar = true;
//        addHeart();
        if (subscription == null) {
            Disposable subscribe = Observable.interval(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            addHeart();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (subscription != null) {
                                if (!subscription.isDisposed()) {
                                    subscription.dispose();
                                    isStar = false;
                                }
                                subscription = null;
                            }
                            start();
                        }
                    });
        }
        if (subscription != null && subscription.isDisposed()) {
            subscriptions.add(subscription);
        }
    }

    public void finishDestroy() {
        if (subscription != null) {
            if (!subscription.isDisposed()) {
                subscription.dispose();
                isStar = false;
            }
            subscription = null;
        }
        if (subscriptions != null) {
            if (!subscriptions.isDisposed()) {
                subscriptions.dispose();
            }
            subscriptions = null;
        }
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PeriscopeLayout);
        mGravity = ta.getInt(R.styleable.PeriscopeLayout_heartGravity, LEFT);
        mHeartMarginLeft = ta.getDimension(R.styleable.PeriscopeLayout_heartMarginLeft, 0);
        mHeartMarginRight = ta.getDimension(R.styleable.PeriscopeLayout_heartMarginRight, 0);
        TypedArray ar = getContext().getResources().obtainTypedArray(ta.getResourceId(R.styleable.PeriscopeLayout_drawableArrays, 0));
        int len = ar.length();
        drawables = new int[len];
        for (int i = 0; i < len; i++)
            drawables[i] = ar.getResourceId(i, 0);
        ar.recycle();
        ta.recycle();

        dHeight = getResources().getDrawable(drawables[0]).getIntrinsicHeight();
        dWidth = getResources().getDrawable(drawables[0]).getIntrinsicWidth();

        lp = new LayoutParams(dWidth, dHeight);
        switch (this.mGravity) {
            case LEFT:
                lp.addRule(ALIGN_PARENT_LEFT, TRUE);
                lp.setMargins((int) mHeartMarginLeft, 0, 0, 0);
                break;
            case CENTER:
                lp.addRule(CENTER_HORIZONTAL, TRUE);
                break;
            case RIGHT:
                lp.addRule(ALIGN_PARENT_RIGHT, TRUE);
                lp.setMargins(0, 0, (int) mHeartMarginRight, 0);
                break;
        }
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
        subscriptions = new CompositeDisposable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addHeart() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(drawables[random.nextInt(drawables.length)]));
        imageView.setLayoutParams(lp);
        addView(imageView);
        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    public void clearView() {
        removeAllViews();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimtor(target);
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AnimatorSet getEnterAnimtor(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ValueAnimator getBezierValueAnimator(View target) {
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        ValueAnimator animator = null;
        switch (this.mGravity) {
            case LEFT:
                animator = ValueAnimator.ofObject(evaluator, new PointF(0 + mHeartMarginLeft, mHeight - dHeight), new PointF(random.nextInt(getWidth() - 100), 0));
                break;
            case CENTER:
                animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth() - 100), 0));
                break;
            case RIGHT:
                animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) - mHeartMarginRight, mHeight - dHeight), new PointF(random.nextInt(getWidth() - 100), 0));
                break;
        }
        animator.addUpdateListener(new BezierListenr(target));
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setTarget(target);
        animator.setDuration(2000);
        return animator;
    }


    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView((target));
        }
    }
}
