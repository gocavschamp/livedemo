package com.fish.live.photo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.live.R;
import com.fish.live.photo.adapter.PhotoAlbumAdapter;
import com.fish.live.photo.adapter.PhotoPopWindowAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.bean.PhotoFolderInfo;
import com.nucarf.base.bean.PhotoInfo;
import com.nucarf.base.bean.PostPhotoEvent;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.DialogUtils;
import com.nucarf.base.utils.FileUtil;
import com.nucarf.base.utils.ImageUtil;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.PhotoTools;
import com.nucarf.base.utils.ScreenUtil;
import com.nucarf.base.utils.ToastUtils;
import com.nucarf.base.widget.recycleview.LRecyclerView;
import com.nucarf.base.widget.recycleview.LRecyclerViewAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Description:相册选择的页面
 * Author: wang
 * Time: 2017/7/17 13:47
 */

public class OpenCameraOrGellaryActivity extends BaseActivity implements PhotoAlbumAdapter.OnItemClickLitener {

    @BindView(R.id.preview_btn)
    TextView previewBtn;
    @BindView(R.id.ensure_tv)
    TextView ensureTv;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    @BindView(R.id.lrecyclerview)
    LRecyclerView lrecyclerview;
    @BindView(R.id.include_left_btn)
    TextView includeLeftBtn;
    @BindView(R.id.phtoto_album_tv)
    TextView phtotoAlbumTv;
    @BindView(R.id.arrow_iv)
    ImageView arrowIv;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.folder_recyclerview)
    RecyclerView folderRecyclerview;
    @BindView(R.id.folder_layout)
    RelativeLayout folderLayout;
    @BindView(R.id.container_layout)
    RelativeLayout containerLayout;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private PhotoAlbumAdapter mAdapter;
    // 所有的相册文件夹
    private ArrayList<PhotoFolderInfo> mAllPhotoFolderList = new ArrayList<>();
    //单个相册文件夹的照片
    private ArrayList<PhotoInfo> mCurPhotoList = new ArrayList<>();
    //所有选中的照片列表
    private ArrayList<String> mSelectedPhotos = new ArrayList<String>();
    //相册文件夹选中的position
    private int mCurrentFolderPosition = 0;
    private final static int SCAN_OK = 1;
    private static final String TAG = "PhotoAlbumActivity.class";
    //手机拍照 request
    public final static int REQUEST_CAMERA = 2000;
    public final static int REQUEST_PHOTO_CROP = 3000;

    // 预览 code
    public final static int REQUEST_PHOTO_PREVIEW = 4000;
    public static final String FROM_CLASS = "fromClass";
    public static final String MAX_SIZE = "maxSize";
    public static final String IS_CROP = "isCrop";
    public static final String CROP_WIDTH = "cropWidth";
    public static final String CROP_HEIGHT = "cropHeight";
    public static final String FROM_TYPE = "fromType";
    //图片文件
    private File mImageFile;
    private String takePhotoFileName = "takePhoto.jpg";
    private PhotoPopWindowAdapter mFolderListAdapter;
    private Animation animation_folder_show;
    private Animation animation_folder_dismiss;
    private String mFromClass;
    private int mMaxSize;
    private boolean mIsCrop = false;
    private int mCropWidth;
    private int mCropHeight;
    //类型 1 帖子 2 发布帖子 添加图片
    private int mFromType;

    public static void launch(Context pContext, String pFromClass, int pMaxSize, boolean pIsCrop, int pCropWidth, int pCropHeight, int pFromType) {
        Intent mIntent = new Intent(pContext, OpenCameraOrGellaryActivity.class);
        mIntent.putExtra(FROM_CLASS, pFromClass);
        mIntent.putExtra(MAX_SIZE, pMaxSize);
        mIntent.putExtra(IS_CROP, pIsCrop);
        mIntent.putExtra(CROP_WIDTH, pCropWidth);
        mIntent.putExtra(CROP_HEIGHT, pCropHeight);
        mIntent.putExtra(FROM_TYPE, pFromType);
        pContext.startActivity(mIntent);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    mAdapter.changeData(mCurPhotoList);
                    setFolderData();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).titleBar(topLayout).init();
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);

        mFromClass = getIntent().getStringExtra(FROM_CLASS);
        mMaxSize = getIntent().getIntExtra(MAX_SIZE, 0);
        mIsCrop = getIntent().getBooleanExtra(IS_CROP, false);
        mFromType = getIntent().getIntExtra(FROM_TYPE, 0);
        int mScreenWidth = ScreenUtil.getScreenWidth(this);
        mCropWidth = getIntent().getIntExtra(CROP_WIDTH, mScreenWidth);
        if (mCropWidth == 0) {
            mCropWidth = mScreenWidth;
        }
        mCropHeight = getIntent().getIntExtra(CROP_HEIGHT, mScreenWidth);
        if (mCropHeight == 0) {
            mCropHeight = mScreenWidth;
        }
        if (mIsCrop) {
            bottom.setVisibility(View.GONE);
        }
        lrecyclerview.setPullRefreshEnabled(false);
        mAdapter = new PhotoAlbumAdapter(mContext, mIsCrop);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mContext, mAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(OpenCameraOrGellaryActivity.this, 4);
        lrecyclerview.setLayoutManager(layoutManager);
        lrecyclerview.setAdapter(mLRecyclerViewAdapter);
        mAdapter.setOnItemClickLitener(this);
        View mHeadView = View.inflate(mContext, R.layout.header_space_view, null);
        mLRecyclerViewAdapter.addHeaderView(mHeadView);
        View mSpaceView = mHeadView.findViewById(R.id.space_view);
        ScreenUtil.setLinearLayoutParams(mSpaceView, ScreenUtil.getScreenWidth(mContext),
                ScreenUtil.dip2px(40) + ScreenUtil.getStatusBarHeight(mContext));
        setBtnStatus(0);
        checkPermisson(1);

    }

    private void checkPermisson(int type) {
        RxPermissions rxPermissions = new RxPermissions(this);
        if (type == 1) {
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                //用户同意使用权限
                                getAllPhoto();
                            } else {
                                //用户不同意使用权限
                                // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                                Toast.makeText(mContext, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        }
                    });
        } else {
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                //用户同意使用权限
                                openCamera();
                            } else {
                                //用户不同意使用权限
                                // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                                Toast.makeText(mContext, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    // 手机拍照
    public void goCamera() {
        int permissionCheck;
        try {
            permissionCheck = ActivityCompat.checkSelfPermission(OpenCameraOrGellaryActivity.this, Manifest.permission.CAMERA);
        } catch (RuntimeException e) {
            ToastUtils.show_middle_pic(this, R.mipmap.icon_delete, "权限错误", ToastUtils.LENGTH_SHORT);
            openCamera();
            return;
        }
        checkPermisson(2);

    }

    //打开照相机
    private void openCamera() {
        if (FileUtil.isExistSdcard(this)) {
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mImageFile = new File(getCacheDir(), System.currentTimeMillis() + takePhotoFileName);
                imageUri = FileProvider.getUriForFile(OpenCameraOrGellaryActivity.this, "com.nucarf.member.fileprovider", mImageFile);
            } else {
                mImageFile = new File(ImageUtil.getCacheDirectory(this), System.currentTimeMillis() + takePhotoFileName);
                imageUri = Uri.fromFile(mImageFile);
            }
            Intent intent = getTakePickIntent(imageUri);
            startActivityForResult(intent, REQUEST_CAMERA);


        } else {
            ToastUtils.show_middle(mContext, getString(R.string.str_chu_cun_ka_cuo_wu), ToastUtils.LENGTH_SHORT);
        }
    }

    private Intent getTakePickIntent(Uri pUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> listCam = pm.queryIntentActivities(intent, 0);
        if (null != listCam) {
            for (ResolveInfo res : listCam) {
                LogUtils.i(TAG, "getTakePickIntent packageName[" + res.activityInfo.packageName + "] name[" + res.activityInfo.name + "]");
                if (res.activityInfo.packageName.contains("com.sec.android.app.camera") || res.activityInfo.packageName.contains("com.android.app.camera")
                        || res.activityInfo.packageName.contains("com.android.camera") || res.activityInfo.packageName.contains("com.miui.camera")) {
                    intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    break;
                }
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pUri);
        return intent;
    }

    //从手机中获取所有的图片
    private void getAllPhoto() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, R.string.str_zan_wu_wai_bu_cun_chu, Toast.LENGTH_SHORT).show();
            return;
        }
        List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(OpenCameraOrGellaryActivity.this);
        mAllPhotoFolderList.addAll(allFolderList);
        if (allFolderList.size() > 0) {
            if (allFolderList.get(0).getPhotoList() != null) {
                mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
            }
        }
        // 通知Handler扫描图片完成
        if (null != mHandler)
            mHandler.sendEmptyMessage(SCAN_OK);
    }

    private void setFolderData() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OpenCameraOrGellaryActivity.this);
        folderRecyclerview.setLayoutManager(layoutManager);
        mFolderListAdapter = new PhotoPopWindowAdapter(mContext);
        folderRecyclerview.setAdapter(mFolderListAdapter);
        mFolderListAdapter.changeData(mAllPhotoFolderList);
        mFolderListAdapter.setOnItemClickLitener(new PhotoPopWindowAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                folderLayout.setVisibility(View.GONE);
                if (mCurrentFolderPosition != position) {
                    mCurrentFolderPosition = position;
                    mCurPhotoList = mAllPhotoFolderList.get(position).getPhotoList();
                    mSelectedPhotos.clear();
                    for (int i = 0; i < mCurPhotoList.size(); i++) {
                        mCurPhotoList.get(i).setChecked(false);
                    }
                    mAdapter.changeData(mCurPhotoList);
                }

            }
        });
    }

    @Subscribe
    public void onEvent(Object pObject) {
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            goCamera();
        } else {
            position = position - 1;
            if (mIsCrop) {
                CropImageActivity.launch(OpenCameraOrGellaryActivity.this, mFromClass, mCurPhotoList.get(position).getPhotoPath(), mCropWidth, mCropHeight, REQUEST_PHOTO_CROP);
            }
        }
    }

    @Override
    public void onSelectClick(int pPosition) {

        PhotoInfo mItemInfo = mCurPhotoList.get(pPosition);
        if (mItemInfo.isChecked()) {
            mItemInfo.setChecked(false);
            mSelectedPhotos.remove(mItemInfo.getPhotoPath());
        } else {
            if (mMaxSize > mSelectedPhotos.size()) {
                mItemInfo.setChecked(true);
                mSelectedPhotos.add(mItemInfo.getPhotoPath());
            } else {
                String content = getString(R.string.str_ninzui_duo_zhi_neng_xuan_ze) + mMaxSize + getString(R.string.str_zhang) + getString(R.string.str_zhao_pian);
                DialogUtils.getInstance().showSelectDialog(mContext, content, "取消", getString(R.string.str_wo_zhi_dao_le), new DialogUtils.DialogClickListener() {
                    @Override
                    public void confirm() {
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        }
        mCurPhotoList.set(pPosition, mItemInfo);
        for (int i = 0; i < mSelectedPhotos.size(); i++) {
            for (int j = 0; j < mCurPhotoList.size(); j++) {
                if (mCurPhotoList.get(j).getPhotoPath().equals(mSelectedPhotos.get(i))) {
                    mCurPhotoList.get(j).setCheckedPosition(i + 1);
                    continue;
                }
            }
        }
        mAdapter.changeData(mCurPhotoList);
        setBtnStatus(mSelectedPhotos.size());
    }

    //设置按钮的状态
    private void setBtnStatus(int pSize) {
        if (pSize > 0) {
            ensureTv.setText(getString(R.string.str_wancheng) + "(" + pSize + ")");
            ensureTv.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ensureTv.setAlpha(1.0f);
            }
            previewBtn.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                previewBtn.setAlpha(1f);
            }
        } else {
            ensureTv.setText(getString(R.string.str_wancheng));
            ensureTv.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ensureTv.setAlpha(0.3f);
            }
            previewBtn.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                previewBtn.setAlpha(0.3f);
            }
        }
    }


    @OnClick({R.id.preview_btn, R.id.ensure_tv, R.id.include_left_btn, R.id.phtoto_album_tv, R.id.folder_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.preview_btn:
                ArrayList<PhotoInfo> mMidPhoto = new ArrayList<>();
                for (int j = 0; j < mCurPhotoList.size(); j++) {
                    for (int i = 0; i < mSelectedPhotos.size(); i++) {
                        if (mSelectedPhotos.get(i).equals(mCurPhotoList.get(j).getPhotoPath())) {
                            mMidPhoto.add(mCurPhotoList.get(j));
                            break;
                        }
                    }
                }
                //二次排序
                ArrayList<PhotoInfo> mEndPhoto = new ArrayList<>();
                for (int i = 0; i < mSelectedPhotos.size(); i++) {
                    for (int j = 0; j < mMidPhoto.size(); j++) {
                        if (mSelectedPhotos.get(i).equals(mMidPhoto.get(j).getPhotoPath())) {
                            mEndPhoto.add(mMidPhoto.get(j));
                            break;
                        }
                    }
                }
//                PhotoScanActivity.launch(mContext, mEndPhoto, mSelectedPhotos, mMaxSize, 0, REQUEST_PHOTO_PREVIEW);

                break;
            case R.id.ensure_tv:
                ensure_action();
                break;
            case R.id.include_left_btn:
                finish();
                break;
            case R.id.phtoto_album_tv:
                if (folderLayout.getVisibility() == View.GONE) {
                    folderLayout.setVisibility(View.VISIBLE);
//                    folderRecyclerview.startAnimation(animation_folder_show);
//                    arrowIv.setRotation(90);
                } else {
                    dissmiss_folder();
                }
                break;
            case R.id.folder_layout:
                dissmiss_folder();
                break;
        }
    }

    private void dissmiss_folder() {
        folderLayout.setVisibility(View.GONE);
    }

    //确认按钮之后的动作
    private void ensure_action() {
        finish();
        EventBus.getDefault().post(new PostPhotoEvent(mSelectedPhotos, true));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == 0) {
                    finish();
                    return;
                }
                // 系统拍照
                if (mImageFile != null && resultCode == -1) {
                    if (mIsCrop) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CropImageActivity.launch(OpenCameraOrGellaryActivity.this, mFromClass, mImageFile.getPath(), mCropWidth, mCropHeight,REQUEST_PHOTO_CROP);
                            }
                        }, 500);
                    } else {
                        mSelectedPhotos.clear();
                        mSelectedPhotos.add(mImageFile.getPath());
                        ensure_action();
                    }
                }
                break;
            case REQUEST_PHOTO_PREVIEW:
                mSelectedPhotos = data.getStringArrayListExtra("selectList");
                ArrayList<PhotoInfo> mendPhotoList = data.getParcelableArrayListExtra("currentPhotoList");
                if (mendPhotoList.size() < mCurPhotoList.size()) {
                    for (int i = 0; i < mCurPhotoList.size(); i++) {
                        for (int j = 0; j < mendPhotoList.size(); j++) {
                            if (mCurPhotoList.get(i).getPhotoPath().equals(mendPhotoList.get(j).getPhotoPath())) {
                                mCurPhotoList.set(i, mendPhotoList.get(j));
                                break;
                            }
                            if (j == mendPhotoList.size() - 1) {
                                mCurPhotoList.get(i).setChecked(false);
                            }

                        }
                    }


                } else {
                    mCurPhotoList = mendPhotoList;
                }

                break;
            case REQUEST_PHOTO_CROP:
                finish();
                break;
            default:
                break;
        }
    }

    public void setSelectCount(int selectSize) {
    }

    @Override
    protected void onDestroy() {
        if (null != mHandler)
            mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
