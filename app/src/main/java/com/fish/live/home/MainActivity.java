package com.fish.live.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fish.live.R;
import com.fish.live.home.presenter.MianPresenter;
import com.fish.live.home.view.MainCotract;
import com.nucarf.base.ui.mvp.BaseMvpActivity;

import java.util.ArrayList;

public class MainActivity extends BaseMvpActivity <MianPresenter> implements MainCotract.View {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {

        mPresenter = new MianPresenter(this);
    }

    @Override
    protected void initViewAndData() {
       addListener();

    }

    @Override
    public void addListener() {

    }

    @Override
    public void setData(ArrayList<String> data) {

    }

    @Override
    public void getMoneySuccess() {

    }

    @Override
    public void getMoneyFail(String message) {

    }

    @Override
    public void closeMoneyDialog() {

    }
}
