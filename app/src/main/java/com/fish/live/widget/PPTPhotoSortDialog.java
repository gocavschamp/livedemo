package com.fish.live.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.fish.live.R;
import com.fish.live.livepush.adapter.PhotoSortAdapter;
import com.fish.live.photo.bean.PhotoBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ${yuwenming} on 2020-12-11 16:53:20.
 */

public class PPTPhotoSortDialog extends DialogFragment {

    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.rl_card_list)
    RelativeLayout rlCardList;
    private View rootView;
    private Activity context;
    private OnDialogClickListener onDialogClickListener;
    private PhotoSortAdapter mAdapter;
    private View view;
    private Unbinder unbinder;
    private ArrayList<PhotoBean> cardBeanArrayList;

    @Override
    public void onStart() {
        super.onStart();
        //设置dialog的 进出 动画
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.ppt_phtot_sort_dialog, container);
        cardBeanArrayList =  getArguments().getParcelableArrayList("list");
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void initData() {
        recycleview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PhotoSortAdapter(R.layout.photo_sort_item_layout,cardBeanArrayList);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recycleview);
        OnItemDragListener onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.black_20));
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                source.itemView.setBackgroundColor(getResources().getColor(R.color.black_20));
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        };
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.ll_content, true);
        mAdapter.setOnItemDragListener(onItemDragListener);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoBean photoBean = mAdapter.getData().get(position);
                photoBean.setChoice(photoBean.isChoice());
                mAdapter.notifyDataSetChanged();

            }
        });
        recycleview.setAdapter(mAdapter);
        mAdapter.setNewData(cardBeanArrayList);
    }

    public void setData(ArrayList<PhotoBean> cardBeanArrayList) {
        mAdapter.setNewData(cardBeanArrayList);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick({R.id.tv_close,R.id.tv_sort,R.id.tv_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_close :
                if (onDialogClickListener != null) {
                    onDialogClickListener.onConfirmClick(cardBeanArrayList);
                    dismiss();
                }
                break;
            case R.id.tv_sort :

                break;
            case R.id.tv_cancle :
                dismiss();
                break;
        }
    }


    public interface OnDialogClickListener {
        void onConfirmClick(ArrayList<PhotoBean> position);
    }
}
