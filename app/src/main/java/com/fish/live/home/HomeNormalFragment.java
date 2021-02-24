package com.fish.live.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.loadingbox.LoadingBox;
import com.fish.live.R;
import com.fish.live.home.adapter.HomeNormalAdapter;
import com.fish.live.home.bean.HomeDataBean;
import com.fish.live.livepush.LivePushActivity;
import com.fish.live.livevideo.LiveVideoActivity;
import com.fish.live.search.RecommentLiveSearchActivity;
import com.fish.live.search.ResentLiveSearchActivity;
import com.nucarf.base.ui.BaseLazyFragment;
import com.nucarf.base.utils.NetUtils;
import com.nucarf.base.utils.UiGoto;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import refresh.swipe.OnRefreshListener;
import refresh.swipe.SwipeToLoadLayout;

/**
 * Created by yuwenming on 2019/10/21.
 */
public class HomeNormalFragment extends BaseLazyFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TYPE = "type";
    @BindView(R.id.refresh_iv)
    ImageView refreshIv;
    @BindView(R.id.pro_bar)
    ProgressBar proBar;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    Unbinder unbinder;
    private HomeNormalAdapter homeAdapter;
    private LoadingBox loadingBox;
    private int mPage = 0;
    private int total_count;
    private String mType = "";
    private Disposable mDisposable;

    public HomeNormalFragment() {
    }

    public static HomeNormalFragment newInstance(String type) {
        HomeNormalFragment myFragment = new HomeNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.home_normal_fragment
                ;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Subscribe
    public void onEvent(Object event) {
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);

        recycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        homeAdapter = new HomeNormalAdapter(new ArrayList<HomeDataBean>());
        recycleview.setAdapter(homeAdapter);
        homeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == 2) {
                UiGoto.startAty(mActivity, LiveVideoActivity.class);
            } else if (position == 3) {
                UiGoto.startAty(mActivity, LivePushActivity.class);
            }
        });
        homeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_more_info:

                    if (homeAdapter.getData().get(position).getItemType() == HomeDataBean.TWO) {
                        UiGoto.startAty(mActivity, ResentLiveSearchActivity.class);
                    } else {
                        UiGoto.startAty(mActivity, RecommentLiveSearchActivity.class);
                    }
                    break;
            }
        });
        swipeToLoadLayout.setLoadMoreEnabled(false);
        swipeToLoadLayout.setOnRefreshListener(this);
        homeAdapter.setOnLoadMoreListener(this, recycleview);
        loadingBox = new LoadingBox(mActivity, swipeToLoadLayout);
        loadingBox.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(mActivity)) {
                    getData(false);
                } else {
                    loadingBox.showInternetErrorLayout();
                }
            }
        });
        if (NetUtils.isNetworkAvailable(mActivity)) {
            getData(false);
        } else {
            loadingBox.showInternetErrorLayout();
        }
    }

    private void getData(boolean isLoadMore) {
        if (!isLoadMore) {
            mPage = 1;
            if (!swipeToLoadLayout.isRefreshing()) {
                showDialog();
            }
        }
//        Map<String, String> map = new TreeMap<String, String>();
//        map.put("limit", "20");
//        map.put("page", "" + mPage);
//        map.put("news_type", "");
//        map.put("member_id", SharePreUtils.getMemberId(BaseAppCache.getContext()) + "");
//        map.put("deviceId", "" + AndroidUtil.getDeviceId(BaseAppCache.getContext()));
//        map.put("token", SharePreUtils.getjwt_token(BaseAppCache.getContext()));
//        String sign = MD5Utils.getSign(map, "wxw");
//        SharePreUtils.setSign(mActivity, sign);
//        AppService client = RetrofitUtils.INSTANCE.getClient(AppService.class);
//        client.getNewList(BaseHttp.getBaseParams(), map).enqueue(new HttpCallBack<BaseResult<MessageTotalBean>>() {
//            @Override
//            public void succeeded(BaseResult<MessageTotalBean> costListBeanBaseResult) {
//                dismissDialog();
//                loadingBox.hideAll();
//                if (!isLoadMore) {
//                    swipeToLoadLayout.setRefreshing(false);
//                } else {
//                    homeAdapter.loadMoreComplete();
//                }
//                mPage++;
//                MessageTotalBean messageTotalBean = costListBeanBaseResult.getResult();
//                total_count = Integer.parseInt(messageTotalBean.getTotal_count());
//                List<MsgListBean> msgListBeanList = messageTotalBean.getList();
//                if (isLoadMore) {
//                    homeAdapter.addData(msgListBeanList);
//                } else {
//                    homeAdapter.setNewData(msgListBeanList);
//                }
//                if (homeAdapter.getData().size() >= total_count) {
//                    homeAdapter.loadMoreEnd();
//                }
//                if (homeAdapter.getData().size() == 0) {
//                    loadingBox.showDataNoLayout();
//                }
//            }
//
//            @Override
//            public void failed(String errorcode) {
//                dismissDialog();
//                if (!isLoadMore) {
//                    if (null != swipeToLoadLayout && swipeToLoadLayout.isRefreshing()) {
//                        swipeToLoadLayout.setRefreshing(false);
//                    }
//                    loadingBox.showInternetErrorLayout();
//                } else {
//                    homeAdapter.loadMoreComplete();
//                    homeAdapter.loadMoreFail();
//                }
//            }
//        });
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (aLong == 1) {
                            dismissDialog();
                            loadingBox.hideAll();
                            if (!isLoadMore) {
                                swipeToLoadLayout.setRefreshing(false);
                            } else {
                                homeAdapter.loadMoreComplete();
                            }
                            mPage++;
                            total_count = 100;
                            List<HomeDataBean> homeDataBeans = new ArrayList<>();
                            if (!isLoadMore) {
                                ArrayList<String> banners = new ArrayList<>();
                                banners.add("");
                                banners.add("");
                                banners.add("");
                                banners.add("");
                                banners.add("");
                                banners.add("");
                                HomeDataBean homeDataBean = new HomeDataBean(1);
                                homeDataBean.setBanners(banners);
                                homeDataBeans.add(homeDataBean);
                            }
                            homeDataBeans.add(new HomeDataBean(isLoadMore ? 3 : 2));
                            homeDataBeans.add(new HomeDataBean(isLoadMore ? 3 : 2));
                            homeDataBeans.add(new HomeDataBean(isLoadMore ? 3 : 2));
                            homeDataBeans.add(new HomeDataBean(isLoadMore ? 3 : 2));
                            homeDataBeans.add(new HomeDataBean(isLoadMore ? 3 : 2));
                            if (isLoadMore) {
                                homeAdapter.addData(homeDataBeans);
                            } else {
                                homeAdapter.setNewData(homeDataBeans);
                            }
                            if (homeAdapter.getData().size() >= total_count) {
                                homeAdapter.loadMoreEnd();
                            }
                            if (homeAdapter.getData().size() == 0) {
                                loadingBox.showDataNoLayout();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        if (!isLoadMore) {
                            if (null != swipeToLoadLayout && swipeToLoadLayout.isRefreshing()) {
                                swipeToLoadLayout.setRefreshing(false);
                            }
                            loadingBox.showInternetErrorLayout();
                        } else {
                            homeAdapter.loadMoreComplete();
                            homeAdapter.loadMoreFail();
                        }
                    }
                });
        addSubscribe(mDisposable);
    }

    @Override
    protected void initView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getData(false);
    }

    @Override
    public void onLoadMoreRequested() {
        if (homeAdapter.getData().size() >= total_count) {
            return;
        } else {
            getData(true);
        }
    }
}
