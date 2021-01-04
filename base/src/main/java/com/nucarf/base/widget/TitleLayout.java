package com.nucarf.base.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.nucarf.base.R;

public class TitleLayout extends RelativeLayout {
    private int title_left_icon;
    private   String title_center_text;
    private   String title_left_text;
    private   String title_right_text;
    private   int title_center_size;
    private   ColorStateList title_center_color;
    ImageView ivLeft;
    RelativeLayout rlLeft;
    TextView tvCenterTitle;
    TextView tvRight;
    ImageView ivRight;
    RelativeLayout rlRight;
    RelativeLayout rlDefaultTitleLayout;

    public TitleLayout(Context context) {
        this(context,null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout, defStyleAttr, 0);
        //获取选中图片
        title_left_icon =  a.getResourceId(R.styleable.TitleLayout_title_left_icon,R.mipmap.icon_arrow_black_back);
        title_center_text = a.getString(R.styleable.TitleLayout_title_center_text);
        title_left_text = a.getString(R.styleable.TitleLayout_title_left_text);
        title_right_text = a.getString(R.styleable.TitleLayout_title_right_text);
        title_center_size = a.getDimensionPixelOffset(R.styleable.TitleLayout_title_center_size, 16); //获取 间隙
        title_center_color = a.getColorStateList(R.styleable.TitleLayout_title_center_color);

        a.recycle();
        initView(context);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context);
//    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.default_title_layout, this);
        ivLeft =   inflate.findViewById(R.id.iv_left);
        rlLeft =   inflate.findViewById(R.id.rl_left);
        rlRight =   inflate.findViewById(R.id.rl_right);
        tvCenterTitle =   inflate.findViewById(R.id.tv_center_title);
        tvRight =   inflate.findViewById(R.id.tv_right);
        rlDefaultTitleLayout =   inflate.findViewById(R.id.rl_default_title_layout);
        rlDefaultTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
        ivLeft.setImageResource(title_left_icon);
        tvCenterTitle.setText(title_center_text);
        tvCenterTitle.setTextSize(title_center_size);
        if(null == title_center_color) {
            tvCenterTitle.setTextColor(getResources().getColor(R.color.color_333333));
        }else {
            tvCenterTitle.setTextColor(title_center_color);
        }
        tvRight.setText(title_right_text);
//        ButterKnife.bind(this, inflate);
    }

    public TitleLayout setTitleBg(int color) {
        rlDefaultTitleLayout.setBackgroundColor(color);
        return this;
    }

    public TitleLayout setTitleText(String titleText) {
        tvCenterTitle.setText(titleText);
        return this;
    }
    public TitleLayout setRightText(String titleText) {
        tvRight.setText(titleText);
        tvRight.setTextColor(getResources().getColor(R.color.color_333333));
        return this;
    }
    public TextView getRightText() {
        return tvRight;
    }

    public TitleLayout setTitleColor(int color) {
        tvCenterTitle.setTextColor(color);
        return this;
    }
    public TitleLayout setLeftPic(int rec) {
        ivLeft.setImageResource(rec);
        return this;
    }

    public TitleLayout setLeftClickListener(OnClickListener onclicklistener) {
        rlLeft.setOnClickListener(onclicklistener);
        return this;

    }

    public TitleLayout setRightClickListener(OnClickListener onclicklistener) {
        rlRight.setOnClickListener(onclicklistener);
        return this;

    }
}
