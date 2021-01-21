package com.fish.live.livepush.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.R;
import com.fish.live.photo.bean.PhotoBean;
import com.nucarf.base.utils.GlideUtils;
import com.nucarf.base.utils.NumberUtils;

import java.util.List;

public class PhotoSortAdapter extends BaseItemDraggableAdapter<PhotoBean, BaseViewHolder> {

    public PhotoSortAdapter(int layout, List<PhotoBean> data) {
        super(layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoBean item) {
        ImageView iv_pic = helper.getView(R.id.iv_pic);
        GlideUtils.load(mContext,item.getPath(),R.mipmap.ic_launcher,iv_pic);
        helper.addOnClickListener(R.id.tv_delete);
    }
}
