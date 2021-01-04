package com.example.loadingbox;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.loadingbox.indicatorview.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.R.attr.tag;

/**
 * Introduce ：加载loading view
 * Author ：yuwenming
 * Time：2017/7/10 16:51
 * Modification Record ：
 */
public class LoadingBox {

    private View mTargetView;
    private View.OnClickListener mClickListener;
    private View.OnClickListener mLoginClickListener;
    private View.OnClickListener mLoginLayoutClickListener;

    private Context mContext;
    private LayoutInflater mInflater;
    private RelativeLayout mContainer;
    private ArrayList<View> mCustomViews;
    private ArrayList<View> mDefaultViews;
    private ViewSwitcher mSwitcher;

    private final String TAG_INTERNET_OFF = "INTERNET_OFF";
    private final String TAG_LOADING_CONTENT = "LOADING_CONTENT";
    private final String TAG_INTERNET_ERROR = "INTERNET_ERROR";
    private final String TAG_DATA_NO = "DATA_NO";

    private final String[] mSupportedAbsListViews = new String[]{"listview", "gridview", "expandablelistview"};
    private final String[] mSupportedViews = new String[]{"linearlayout", "relativelayout", "framelayout", "scrollview", "nestedscrollview", "recyclerview", "viewgroup", "swipetoloadlayout", "webview", "viewpager"};

    public LoadingBox(Context context, View targetView) {
        this.mContext = context;
        this.mInflater = ((Activity) mContext).getLayoutInflater();
        this.mTargetView = targetView;
        this.mContainer = new RelativeLayout(mContext);
        this.mCustomViews = new ArrayList<View>();
        this.mDefaultViews = new ArrayList<View>();

        Class viewClass = mTargetView.getClass();
        Class superViewClass = viewClass.getSuperclass();
        String viewType = viewClass.getName().substring(viewClass.getName().lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());
        String superViewType = superViewClass.getName().substring(superViewClass.getName().lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());

        if (Arrays.asList(mSupportedAbsListViews).contains(viewType) || Arrays.asList(mSupportedAbsListViews).contains(superViewType))
            initializeAbsListView();
        else if (Arrays.asList(mSupportedViews).contains(viewType) || Arrays.asList(mSupportedViews).contains(superViewType))
            initializeViewContainer();
        else
            throw new IllegalArgumentException("TargetView type [" + superViewType + "] is not supported !");
    }

    public LoadingBox(Context context, int viewID) {
        this.mContext = context;
        this.mInflater = ((Activity) mContext).getLayoutInflater();
        this.mTargetView = mInflater.inflate(viewID, null, false);
        this.mContainer = new RelativeLayout(mContext);
        this.mCustomViews = new ArrayList<View>();
        this.mDefaultViews = new ArrayList<View>();

        Class viewClass = mTargetView.getClass();
        Class superViewClass = viewClass.getSuperclass();
        String viewType = viewClass.getName().substring(viewClass.getName().lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());
        String superViewType = superViewClass.getName().substring(superViewClass.getName().lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());

        if (Arrays.asList(mSupportedAbsListViews).contains(viewType) || Arrays.asList(mSupportedAbsListViews).contains(superViewType))
            initializeAbsListView();
        else if (Arrays.asList(mSupportedViews).contains(viewType) || Arrays.asList(mSupportedViews).contains(superViewType))
            initializeViewContainer();
        else
            throw new IllegalArgumentException("TargetView type [" + superViewType + "] is not supported !");
    }

    private void initializeViewContainer() {
        setDefaultViews();
        mSwitcher = new ViewSwitcher(mContext);
        ViewSwitcher.LayoutParams params = new ViewSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ViewGroup group = (ViewGroup) mTargetView.getParent();
        int index = 0;
        Clonner target = new Clonner(mTargetView);
        Clonner1 targetLayoutParams= new Clonner1(mTargetView.getLayoutParams());
        if (group != null) {
            index = group.indexOfChild(mTargetView);
            group.removeView(mTargetView);
        }

        target.getmView().setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mSwitcher.addView(mContainer, 0);
        mSwitcher.addView(target.getmView(), 1);
        mSwitcher.setDisplayedChild(1);

        if (group != null) {
            group.addView(mSwitcher, index, targetLayoutParams.layoutParams);
        } else {
            ((Activity) mContext).setContentView(mSwitcher);
        }
    }

    private void setDefaultViews() {
        View mLayoutInternetOff = initView(R.layout.exception_no_internet, TAG_INTERNET_OFF);
        View mLayoutLoadingContent = initView(R.layout.exception_loading_content, TAG_LOADING_CONTENT);
        View mLayoutInternetError = initView(R.layout.exception_failure, TAG_INTERNET_ERROR);
        View mLayoutDataNo = initView(R.layout.exception_no_data, TAG_DATA_NO);


        mDefaultViews.add(0, mLayoutInternetOff);
        mDefaultViews.add(1, mLayoutLoadingContent);
        mDefaultViews.add(2, mLayoutInternetError);
        mDefaultViews.add(3, mLayoutDataNo);


        mLayoutInternetOff.setVisibility(View.GONE);
        mLayoutLoadingContent.setVisibility(View.GONE);
        mLayoutInternetError.setVisibility(View.GONE);
        mLayoutDataNo.setVisibility(View.GONE);

        RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        containerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        containerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        mContainer.setLayoutParams(containerParams);

        mContainer.addView(mLayoutLoadingContent);
        mContainer.addView(mLayoutInternetOff);
        mContainer.addView(mLayoutInternetError);
        mContainer.addView(mLayoutDataNo);
    }

    private void initializeAbsListView() {
        setDefaultViews();
        AbsListView abslistview = (AbsListView) mTargetView;
        abslistview.setVisibility(View.GONE);
        ViewGroup parent = (ViewGroup) abslistview.getParent();
        if (mContainer != null) {
            parent.addView(mContainer);
            abslistview.setEmptyView(mContainer);
        } else
            throw new IllegalArgumentException("mContainer is null !");
    }


    public void showLoadingLayout() {
        show(TAG_LOADING_CONTENT);
        (mDefaultViews.get(1).findViewById(R.id.exception_message)).setVisibility(View.GONE);
    }

    public void showLoadingLayoutTransparency() {
        show(TAG_LOADING_CONTENT);
    }

    public void showInternetOffLayout() {
        show(TAG_INTERNET_OFF);
    }

    public void showInternetErrorLayout() {
        show(TAG_INTERNET_ERROR);
    }

    public void showDataNoLayout() {
        show(TAG_DATA_NO);
    }


    public void showCustomView(String tag) {
        show(tag);
    }


    public void hideAll() {
        ArrayList<View> views = new ArrayList<View>(mDefaultViews);
        views.addAll(mCustomViews);
        for (View view : views) {
            if (view.getTag() != null && view.getTag().toString().equals(tag)) {
                view.setVisibility(View.VISIBLE);
                if (view.getTag().equals(TAG_LOADING_CONTENT)) {
                    AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
                    avi.hide();
                }
            }
            view.setVisibility(View.GONE);
        }
        if (mSwitcher != null) {
            mSwitcher.setDisplayedChild(1);
        }
    }

    private void show(String tag) {
        ArrayList<View> views = new ArrayList<View>(mDefaultViews);
        views.addAll(mCustomViews);
        for (View view : views) {
            if (view.getTag() != null && view.getTag().toString().equals(tag)) {
                view.setVisibility(View.VISIBLE);
                if (view.getTag().toString().equals(TAG_LOADING_CONTENT)) {
                    AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
                    avi.setIndicatorColor(R.color.color_mainred);
                    avi.setIndicator("BallPulseIndicator");
                    avi.show();
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
        if (mSwitcher != null && mSwitcher.getDisplayedChild() != 0) {
            mSwitcher.setDisplayedChild(0);
        }
    }

    private View initView(int layout, String tag) {
        View view = mInflater.inflate(layout, null, false);

        view.setTag(tag);
        view.setVisibility(View.GONE);

        View buttonView = view.findViewById(R.id.exception_retry);
        if (buttonView != null)
            buttonView.setOnClickListener(this.mClickListener);

        return view;
    }

    public void setLoadingMessage(String message) {
        (mDefaultViews.get(1).findViewById(R.id.exception_message)).setVisibility(View.VISIBLE);
        ((TextView) mDefaultViews.get(1).findViewById(R.id.exception_message)).setText(message);
    }

    public void setExceptionBackgroundColor(int color) {
        for (View view : mDefaultViews) {
            View retryView = view.findViewById(R.id.exception_root);
            if (retryView != null)
                retryView.setBackgroundColor(color);
        }
    }

    public void setExceptionBackgroundImage(int resource) {
        for (View view : mDefaultViews) {
            View retryView = view.findViewById(R.id.exception_root);
            if (retryView != null)
                retryView.setBackgroundResource(resource);
        }
    }

    public void setExceptionTextColor(int color) {
        for (View view : mDefaultViews) {
            TextView title = (TextView) view.findViewById(R.id.exception_title);
            if (title != null)
                title.setTextColor(color);
            TextView message = (TextView) view.findViewById(R.id.exception_message);
            if (message != null)
                message.setTextColor(color);
        }
    }

    public void setStatePicImg(int image) {
        ((ImageView) mDefaultViews.get(3).findViewById(R.id.iv_state_pic)).setImageResource(image);
    }

    public void setStateMessage(String message) {
        ((TextView) mDefaultViews.get(3).findViewById(R.id.exception_title)).setText(message);
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.mClickListener = clickListener;
        for (View view : mDefaultViews) {
            View retryView = view.findViewById(R.id.exception_retry);
            if (retryView != null)
                retryView.setOnClickListener(this.mClickListener);
        }
    }

    public void setLoginClickListener(View.OnClickListener clickListener) {
        this.mLoginClickListener = clickListener;
        for (View view : mDefaultViews) {
//            View retryView = view.findViewById(R.id.go_login);
//            if (retryView != null)
//                retryView.setOnClickListener(this.mLoginClickListener);
        }
    }

    public void setLoginLayoutClickListener(View.OnClickListener clickListener) {
        this.mLoginLayoutClickListener = clickListener;
//        for (View view : mDefaultViews) {
//            View retryView = view.findViewById(R.id.login_layout);
//            if (retryView != null)
//                retryView.setOnClickListener(this.mLoginLayoutClickListener);
//        }
    }

    public void addCustomView(View customView, String tag) {
        customView.setTag(tag);
        customView.setVisibility(View.GONE);
        mCustomViews.add(customView);
        mContainer.addView(customView);
    }

    public void addCustomView(int resId, String tag) {
        View customView = LayoutInflater.from(mContext).inflate(resId, null);
        customView.setTag(tag);
        customView.setVisibility(View.GONE);
        mCustomViews.add(customView);
        mContainer.addView(customView);
    }


    private class Clonner {
        private View mView;

        public Clonner(View view) {
            this.setmView(view);
        }

        public View getmView() {
            return mView;
        }

        public void setmView(View mView) {
            this.mView = mView;
        }
    }

    private class Clonner1 {
        private ViewGroup.LayoutParams layoutParams;


        public Clonner1(ViewGroup.LayoutParams layoutParams) {
            this.setmView(layoutParams);

        }

        public ViewGroup.LayoutParams getmView() {
            return layoutParams;
        }

        public void setmView(ViewGroup.LayoutParams mView) {
            this.layoutParams = mView;
        }
    }
}
