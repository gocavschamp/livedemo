package com.fish.live.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fish.live.R;
import com.nucarf.base.bean.PhotoInfo;
import com.nucarf.base.utils.GlideUtils;
import com.nucarf.base.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 相册选择的adapter
 * Author: wang
 * Time: 2017/7/17 13:56
 */

public class PhotoAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {


    private Context mContext;
    private List<PhotoInfo> mCurPhotoList = new ArrayList<>();
    private ArrayList<String> mSelectedPhotos;
    private boolean mCrop;
    private boolean mHasCameraBtn = true;

    public PhotoAlbumAdapter(Context pContext, boolean isCrop) {
        this.mContext = pContext;
        this.mCrop = isCrop;
    }

    //设置是否含有相机按钮
    public void setmHasCameraBtn(boolean pIsHasCameraBtn) {
        mHasCameraBtn = pIsHasCameraBtn;
    }

    public void setselectData(ArrayList<String> mSelectedPhotos) {
        this.mSelectedPhotos = mSelectedPhotos;
        notifyDataSetChanged();
    }

    public void changeData(List<PhotoInfo> pCurPhotoList) {
        this.mCurPhotoList = pCurPhotoList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(inflate(parent, R.layout.photo_album_item_select_imageview));
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Viewholder mHolder = (Viewholder) holder;
        mHolder.imgLayout.setTag(position);

        if (position == 0) {
//            Glide.with(mContext).load(R.mipmap.take_photo).centerCrop().into(((Viewholder) holder).sqimgeView);
            mHolder.checkRl.setVisibility(View.GONE);
            mHolder.sqimgeView.setImageResource(R.mipmap.icon_upload);
        } else if (position > 0) {
            final int Position = position - 1;
            PhotoInfo mItem = mCurPhotoList.get(Position);
            GlideUtils.load(mContext, mItem.getPhotoPath(), mHolder.sqimgeView);
            mHolder.checkRl.setVisibility(View.VISIBLE);
            if (mItem.isChecked()) {
                mHolder.checkTv.setText(mItem.getCheckedPosition() + "");
                mHolder.checkTv.setBackgroundResource(R.drawable.round_maincolor_5);
            } else {
                mHolder.checkTv.setText("");
                mHolder.checkTv.setBackgroundResource(R.mipmap.circle_icon);
            }
            mHolder.checkRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOnItemClickLitener) {
                        mOnItemClickLitener.onSelectClick(Position);
                    }
                }
            });
        }
        if (mCrop) {
            mHolder.checkRl.setVisibility(View.GONE);
        }
        mHolder.imgLayout.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return mCurPhotoList.size() + 1;
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.sqimge_view)
        SquareImageView sqimgeView;
        @BindView(R.id.conver_img)
        View converImg;
        @BindView(R.id.check_tv)
        TextView checkTv;
        @BindView(R.id.check_rl)
        RelativeLayout checkRl;
        @BindView(R.id.img_layout)
        RelativeLayout imgLayout;

        Viewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_layout:
                if (null != mOnItemClickLitener) {
                    RelativeLayout mImageLayout = (RelativeLayout) view;
                    int position = (Integer) mImageLayout.getTag();
                    mOnItemClickLitener.onItemClick(position);
                }
                break;
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);

        void onSelectClick(int pPosition);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private boolean isInSelectedDataList(String selectedString) {
        for (int i = 0; i < mSelectedPhotos.size(); i++) {
            if (mSelectedPhotos.get(i).equals(selectedString)) {
                return true;
            }
        }
        return false;
    }
}
