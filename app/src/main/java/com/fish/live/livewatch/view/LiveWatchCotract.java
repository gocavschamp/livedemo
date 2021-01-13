package com.fish.live.livewatch.view;


import com.nucarf.base.ui.mvp.BasePersenter;
import com.nucarf.base.ui.mvp.BaseView;

import java.util.ArrayList;

public interface LiveWatchCotract {
    interface View extends BaseView {

        void setData(ArrayList<String> data);

        void getMoneySuccess();

    }

    interface Presenter extends BasePersenter<View> {
        void loadData(boolean isRefresh);

    }
}
