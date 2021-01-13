package com.fish.live.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.deadline.statebutton.StateButton;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.database.db.DaoSession;
import com.fish.live.database.db.SearchHistoryBeanDao;
import com.fish.live.search.adapter.SearchHistoryAdapter;
import com.fish.live.search.adapter.SearchResultAdapter;
import com.fish.live.search.bean.SearchHistoryBean;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nucarf.base.ui.BaseActivity;
import com.nucarf.base.utils.DialogUtils;
import com.nucarf.base.utils.KeyboardUtil;
import com.nucarf.base.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements OnTabSelectListener, TextWatcher {

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
    private SearchHistoryAdapter historyAdapter;
    private SearchResultAdapter resultAdapter;
    private SearchResultAdapter resultRecommentAdapter;
    private List<SearchHistoryBean> historyList;

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
        resultRecommentAdapter = new SearchResultAdapter(R.layout.live_list_item_layout);
        rvResult.setAdapter(resultAdapter);
        tabLayout.setOnTabSelectListener(this);
        input.addTextChangedListener(this);
        historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.delete :
                    DaoSession daoSession = ((LiveApplication) getApplication()).getDaoSession();
                    SearchHistoryBeanDao searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();
                    List<SearchHistoryBean> list = searchHistoryBeanDao.queryBuilder()
                            .where(SearchHistoryBeanDao.Properties.Name
                                    .eq(historyAdapter.getData().get(position))).list();
                    searchHistoryBeanDao.deleteInTx(list);
                    getRecentData();
                    break;
                case R.id.text :
                    KeyboardUtil.hideSoftInput(input.getWindowToken(), SearchActivity.this);
                    input.setText(historyAdapter.getData().get(position));
//                    searchStation(true, historyAdapter.getData().get(position));
                    break;
            }

        });
    }

    @OnClick({R.id.go_search, R.id.tv_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_search:
                //todo   search
                addHistory();
                break;
            case R.id.tv_clear_all:
                //clear  history
                showClearAlert();
                break;
        }
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
                rvResult.setAdapter(resultAdapter);
                break;
            case 1:
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
}
