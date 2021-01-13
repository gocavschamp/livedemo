package com.fish.live.search.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deadline.statebutton.StateButton;
import com.fish.live.R;
import com.nucarf.base.bean.StringBean;

public class CardFilterAdapter extends BaseQuickAdapter<StringBean, BaseViewHolder> {

    public CardFilterAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StringBean item) {
        StateButton tv_name = helper.getView(R.id.tv_name);
        tv_name.setPressed(item.isChoice());
        tv_name.setText("精神科");
//        TextView tv_oil_price = helper.getView(R.id.tv_oil_price);
//        tv_name.setSelected(item.isChoice());
//        tv_oil_price.setSelected(item.isChoice());
//        helper.setText(R.id.tv_name, item.getCategory_name());
//        helper.setGone(R.id.tv_oil_price, false);

    }

}
