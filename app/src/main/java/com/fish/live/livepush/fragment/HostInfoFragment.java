package com.fish.live.livepush.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fish.live.R;
import com.fish.live.livepush.adapter.HostListAdapter;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.ui.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class HostInfoFragment extends BaseLazyFragment {
    @BindView(R.id.rv_host_list)
    RecyclerView rvHostList;
    @BindView(R.id.time_str)
    TextView timeStr;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.info_str)
    TextView infoStr;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.image_info)
    ImageView imageInfo;
    private HostListAdapter hostListAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.host_info_fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        rvHostList.setLayoutManager(new LinearLayoutManager(mActivity,RecyclerView.HORIZONTAL,false));
        hostListAdapter = new HostListAdapter(R.layout.item_host_info_layout);
        rvHostList.setAdapter(hostListAdapter);
        List<StringBean> hostData = new ArrayList<>();
        hostData.add(new StringBean());
        hostData.add(new StringBean());
        hostData.add(new StringBean());
        hostListAdapter.setNewData(hostData);
        hostListAdapter.setOnItemClickListener((adapter, view, position) -> {
            showHostInfo(hostData.get(position));

        });

    }

    private void showHostInfo(StringBean stringBean) {

    }
}
