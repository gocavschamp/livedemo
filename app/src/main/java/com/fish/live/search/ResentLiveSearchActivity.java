package com.fish.live.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.deadline.statebutton.StateButton;
import com.example.loadingbox.LoadingBox;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.database.db.DaoSession;
import com.fish.live.database.db.SearchHistoryBeanDao;
import com.fish.live.home.bean.HomeDataBean;
import com.fish.live.search.adapter.SearchHistoryAdapter;
import com.fish.live.search.adapter.SearchResultAdapter;
import com.fish.live.search.bean.SearchHistoryBean;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.DialogUtils;
import com.nucarf.base.utils.KeyboardUtil;
import com.nucarf.base.utils.NetUtils;
import com.nucarf.base.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 近期直播搜索
 */
public class ResentLiveSearchActivity extends BaseActivity implements OnTabSelectListener, TextWatcher, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.go_search)
    TextView goSearch;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.tv_clear_all)
    StateButton tvClearAll;
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    @BindView(R.id.result_layout)
    LinearLayout resultLayout;
    @BindView(R.id.history_layout)
    LinearLayout historyLayout;
    private SearchHistoryAdapter historyAdapter;
    private SearchResultAdapter resultAdapter;//全部直播
    private SearchResultAdapter resultRecommentAdapter;//推荐
    private List<SearchHistoryBean> historyList;
    private Disposable mDisposable;
    private LoadingBox loadingBox;
    private int mPage;
    private int total_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        ScreenUtil.setRecycleviewLinearLayout(mContext, rvHistory, true);
        ScreenUtil.setRecycleviewLinearLayout(mContext, rvResult, true);
        historyAdapter = new SearchHistoryAdapter(R.layout.item_history_layout);
        rvHistory.setAdapter(historyAdapter);
        resultAdapter = new SearchResultAdapter(R.layout.live_list_item_layout);
        resultAdapter.setEnableLoadMore(true);
        resultAdapter.setOnLoadMoreListener(this, rvResult);
        resultRecommentAdapter = new SearchResultAdapter(R.layout.live_list_item_layout);
        rvResult.setAdapter(resultAdapter);
        tabLayout.setOnTabSelectListener(this);
        tabLayout.setTabData(new String[]{"推荐", "全部直播"});
        input.addTextChangedListener(this);
        historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.delete:
                    DaoSession daoSession = ((LiveApplication) getApplication()).getDaoSession();
                    SearchHistoryBeanDao searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();
                    List<SearchHistoryBean> list = searchHistoryBeanDao.queryBuilder()
                            .where(SearchHistoryBeanDao.Properties.Name
                                    .eq(historyAdapter.getData().get(position))).list();
                    searchHistoryBeanDao.deleteInTx(list);
                    getRecentData();
                    break;
                case R.id.text:
                    KeyboardUtil.hideSoftInput(input.getWindowToken(), ResentLiveSearchActivity.this);
                    input.setText(historyAdapter.getData().get(position));
//                    searchStation(true, historyAdapter.getData().get(position));
                    break;
            }

        });
//        getRecentData();
        loadingBox = new LoadingBox(this, rvResult);
        loadingBox.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(mContext)) {
                    getData(false);
                } else {
                    loadingBox.showInternetErrorLayout();
                }
            }
        });
        if (NetUtils.isNetworkAvailable(mContext)) {
            getData(false);
        } else {
            loadingBox.showInternetErrorLayout();
        }
    }

    @OnClick({R.id.go_search, R.id.tv_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_search:
               getData(true);
                break;
            case R.id.tv_clear_all:
                //clear  history
                showClearAlert();
                break;
        }
    }

    private void getData(boolean isLoadMore) {
        if (!isLoadMore) {
            mPage = 1;
            showDialog();
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
                            if (isLoadMore) {
                                resultAdapter.loadMoreComplete();
                            }
                            mPage++;
                            total_count = 100;
                            ArrayList<StringBean> stringBeans = new ArrayList<>();
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            stringBeans.add(new StringBean());
                            if (isLoadMore) {
                                resultAdapter.addData(stringBeans);
                            } else {
                                resultAdapter.setNewData(stringBeans);
                            }
                            if (resultAdapter.getData().size() >= total_count) {
                                resultAdapter.loadMoreEnd();
                            }
                            if (resultAdapter.getData().size() == 0) {
                                loadingBox.showDataNoLayout();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissDialog();
                        if (!isLoadMore) {
                            loadingBox.showInternetErrorLayout();
                        } else {
                            resultAdapter.loadMoreComplete();
                            resultAdapter.loadMoreFail();
                        }
                    }
                });
        addSubscribe(mDisposable);
    }

    private void addHistory() {
        KeyboardUtil.hideSoftInput(input.getWindowToken(), this);
        SearchHistoryBeanDao searchHistoryBeanDao = ((LiveApplication) getApplication()).getDaoSession().getSearchHistoryBeanDao();
        SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
        searchHistoryBean.setName(input.getText().toString());
        searchHistoryBeanDao.insertOrReplace(searchHistoryBean);
        getRecentData();
    }

    private void getRecentData() {
        DaoSession daoSession = ((LiveApplication) getApplication()).getDaoSession();
        historyList = daoSession.getSearchHistoryBeanDao().queryBuilder().orderDesc(SearchHistoryBeanDao.Properties.Id).limit(10).list();
        List<String> history = new ArrayList<>();
        for (int i = 0; i < historyList.size(); i++) {
            history.add(historyList.get(i).getName());
        }
        historyAdapter.setNewData(history);
//        historyAdapter.setEmptyView(historyEmptyView);
//        if (history.size() > 0) {
//            if (historyAdapter.getFooterViewsCount() == 0) {
//                historyAdapter.addFooterView(footer);
//            }
//        } else {
//            historyAdapter.removeAllFooterView();
//        }
    }

    private void showClearAlert() {
        DialogUtils.getInstance().showSelectDialog(mContext, "您确定要清除记录吗？", "取消", "确定", new DialogUtils.DialogClickListener() {
            @Override
            public void confirm() {
                DaoSession daoSession = ((LiveApplication) getApplication()).getDaoSession();
                List<SearchHistoryBean> list = daoSession.getSearchHistoryBeanDao().queryBuilder().build().list();
                daoSession.getSearchHistoryBeanDao().deleteInTx(list);
                getRecentData();
            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                getData(false);
                rvResult.setAdapter(resultAdapter);
                break;
            case 1:
                getData(false);
                rvResult.setAdapter(resultRecommentAdapter);
                break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onLoadMoreRequested() {
        if (resultAdapter.getData().size() >= total_count) {
            return;
        } else {
            getData(true);
        }
    }
}
