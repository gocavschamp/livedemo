package com.fish.live.search.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.R;
import com.nucarf.base.bean.StringBean;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/13 10:50
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchHistoryAdapter(int layout) {
        super(layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.text,item)
                .addOnClickListener(R.id.text,R.id.delete);
    }
}
