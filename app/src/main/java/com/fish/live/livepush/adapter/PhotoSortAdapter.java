package com.fish.live.livepush.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.R;
import com.fish.live.photo.bean.PhotoBean;
import com.nucarf.base.utils.NumberUtils;

import java.util.List;

public class PhotoSortAdapter extends BaseItemDraggableAdapter<PhotoBean, BaseViewHolder> {
    private int mType = 0;//  0 转油 1加油

    public PhotoSortAdapter(int layout, List<PhotoBean> data) {
        super(layout,data);
    }

    public void setType(int type) {
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoBean item) {
    }
}
