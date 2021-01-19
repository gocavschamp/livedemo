package com.fish.live.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fish.live.R;
import com.fish.live.photo.bean.PhotoEvent;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.FileUtil;
import com.nucarf.base.utils.ImageUtil;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.widget.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropImageActivity extends BaseActivity {

    public static final String FROMCLASSNAME = "fromclassname";
    @BindView(R.id.cropImg)
    CropImageView cropImg;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.ok)
    TextView ok;
//    @Bind(R.id.cropImg)
//    CropImageView cropImg;
//    @Bind(R.id.cancel)
//    TextView cancel;
//    @Bind(R.id.ok)
//    TextView ok;
    /**
     * 图片的路径
     */
    private String img_url;
    /**
     * 缓存的图片
     */
    private Bitmap tmpBitmap = null;

    private int mBitmapW = 1024;
    private int mBitmapH = 1024;


    public static void launch(Activity context, String pFromClassName, String img_url, int targetWidth, int targetHeight, int requestCode) {
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra("img_url", img_url);
        intent.putExtra(FROMCLASSNAME, pFromClassName);
        intent.putExtra("targetWidth", targetWidth);
        intent.putExtra("targetHeight", targetHeight);
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_crop);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        img_url =   getIntent().getStringExtra("img_url");
        mBitmapW =  getIntent().getIntExtra("targetWidth", mBitmapW);
        mBitmapH =  getIntent().getIntExtra("targetHeight", mBitmapH);
        if (img_url != "" || img_url != null) {
            tmpBitmap = ImageUtil.geAutoRotatedBitmap(img_url, mBitmapH, mBitmapW);
            if (null == tmpBitmap) {
                finish();

            }
            cropImg.setDrawable(new BitmapDrawable(getResources(), tmpBitmap), mBitmapW, mBitmapH);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tmpBitmap != null) {
            tmpBitmap.recycle();
            tmpBitmap = null;
        }
        if (cropImg != null) {
            cropImg = null;
        }
        System.gc();
    }

    @OnClick({R.id.cancel, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                EventBus.getDefault().post(new PhotoEvent("", getIntent().getStringExtra(FROMCLASSNAME), "canceled"));
                finish();
                break;
            case R.id.ok:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String cacheDirectory = ImageUtil.getCacheDirectory(mContext) + File.separator + "crop";
                            FileUtil.deleteFile(cacheDirectory);
                            String fileName = System.currentTimeMillis() + ".jpg";
                            String destPath = cacheDirectory + File.separator + fileName;
                            FileUtil.writeImage(cropImg.getCropImage(), destPath, 100);
                            LogUtils.e("裁切后图片路径", destPath);
                            EventBus.getDefault().post(new PhotoEvent(destPath, getIntent().getStringExtra(FROMCLASSNAME), "ok"));
                            setResult(RESULT_CANCELED);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PhotoEvent("", getIntent().getStringExtra(FROMCLASSNAME), "canceled"));
        super.onBackPressed();
    }
}
