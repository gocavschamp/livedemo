package com.fish.live.home.view;


import com.nucarf.base.ui.mvp.BasePersenter;
import com.nucarf.base.ui.mvp.BaseView;

import java.util.ArrayList;

public interface MainCotract {
    interface View extends BaseView {

        void setData(ArrayList<String> data);

        void getMoneySuccess();

        void getMoneyFail(String message);

        void closeMoneyDialog();
    }

    interface Presenter extends BasePersenter<View> {
        void loadData(boolean isRefresh);

    }
}
