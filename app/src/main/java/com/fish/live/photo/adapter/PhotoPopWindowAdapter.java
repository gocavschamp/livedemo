package com.fish.live.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fish.live.R;
import com.nucarf.base.bean.PhotoFolderInfo;
import com.nucarf.base.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WANG on 2016/5/5.
 */
public class PhotoPopWindowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<PhotoFolderInfo> mAllPhotoFolderList = new ArrayList<>();

    public PhotoPopWindowAdapter(Context pContext) {
        this.mContext = pContext;
    }

    public void changeData(List<PhotoFolderInfo> pAllPhotoFolderList) {
        this.mAllPhotoFolderList = pAllPhotoFolderList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewholder(inflate(parent, R.layout.photofolder_list_item));
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoFolderInfo item = mAllPhotoFolderList.get(position);
        GlideUtils.load(mContext, item.getCoverPhoto().getPhotoPath(), ((viewholder) holder).firstImg);
        ((viewholder) holder).folderName.setText(item.getFolderName() + "（"+mContext.getString(R.string.str_gong) + item.getPhotoList().size() + mContext.getString(R.string.str_zhang)+"）");
        ((viewholder) holder).itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return null != mAllPhotoFolderList ? mAllPhotoFolderList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickLitener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickLitener.onItemClick(v, (int) v.getTag());
        }
    }


    static class viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.first_img)
        ImageView firstImg;
        @BindView(R.id.folder_name)
        TextView folderName;
        public View itemView;

        viewholder(View view) {
            super(view);
            itemView = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
