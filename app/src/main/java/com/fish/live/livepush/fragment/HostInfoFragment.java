package com.fish.live.livepush.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fish.live.R;
import com.fish.live.home.HomeNormalFragment;
import com.fish.live.livepush.adapter.HostListAdapter;
import com.fish.live.widget.HostInfoDialog;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.ui.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class HostInfoFragment extends BaseLazyFragment {
    private static final String TYPE = "host";
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
    private HostInfoDialog hostInfoDialog;
    private Unbinder unbinder;

    @Override
    protected int setLayoutId() {
        return R.layout.host_info_fragment;
    }
    public HostInfoFragment() {
    }

    public static HostInfoFragment newInstance(String type) {
        HostInfoFragment myFragment = new HostInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, mRootView);
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
        hostInfoDialog = new HostInfoDialog();

    }

    private void showHostInfo(StringBean stringBean) {
        Bundle args = new Bundle();
        args.putSerializable("data", stringBean);
        hostInfoDialog.setArguments(args);
        hostInfoDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.oilCardDialogStyle);
        hostInfoDialog.show(getFragmentManager(), "hostinfo");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
