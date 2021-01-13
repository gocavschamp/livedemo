package com.fish.live.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fish.live.R;
import com.fish.live.search.adapter.CardFilterAdapter;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.utils.ScreenUtil;

import java.util.List;


public class CardFilterPopupWindow extends PopupWindow {
    private Context context;
    private View mMenuView;
    private OnItemClickListener listener;
    private RecyclerView recycleView;
    private final CardFilterAdapter cardFilterAdapter;

    public CardFilterPopupWindow(Context context, List<StringBean> data) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mMenuView = inflater.inflate(R.layout.card_filter_popwindow_layout, null);
        recycleView = mMenuView.findViewById(R.id.recycleview);
        //设置允许弹出窗口超出屏幕范围
        this.setClippingEnabled(false);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.black_80));
        this.setBackgroundDrawable(dw);
        mMenuView.setPadding(0, 0, 0, ScreenUtil.getNavigationBarSize(context).y);

        recycleView.setLayoutManager(new GridLayoutManager(context,4, LinearLayoutManager.VERTICAL, false));
        cardFilterAdapter = new CardFilterAdapter(R.layout.card_filter_item_layout);
        cardFilterAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setChoice(i == position);
            }
            cardFilterAdapter.notifyDataSetChanged();
            if (listener != null) {
                listener.clickItem(position);
            }
            dismiss();
        });

        recycleView.setAdapter(cardFilterAdapter);
        cardFilterAdapter.setNewData(data);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = mMenuView.findViewById(R.id.recycleview).getTop();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void setMcType(int mc_type) {
        if (null != cardFilterAdapter) {
            cardFilterAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 获取内容组件
     *
     * @return
     */
    public RecyclerView getContent() {
        return recycleView;
    }

    /**
     * 展示位置
     *
     * @param view
     */
    public void showAtBottom(View view) {
        if (Build.VERSION.SDK_INT < 24) {
            this.showAsDropDown(view);
        } else {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            this.showAtLocation(view, Gravity.NO_GRAVITY, 0, y + view.getHeight());
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void clickItem(int position);
    }
}
