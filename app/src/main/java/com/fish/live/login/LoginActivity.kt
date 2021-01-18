package com.fish.live.login

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.fish.live.R
import com.fish.live.bean.LoginBean
import com.fish.live.home.MainActivity
import com.fish.live.service.AppService
import com.nucarf.base.retrofit.CommonSubscriber
import com.nucarf.base.retrofit.CommonSubscriberTemp
import com.nucarf.base.retrofit.RetrofitUtils
import com.nucarf.base.retrofit.RxSchedulers
import com.nucarf.base.ui.BaseActivityWithTitle
import com.nucarf.base.utils.SharePreUtils
import com.nucarf.base.utils.UiGoto
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_input_text.*

class LoginActivity : BaseActivityWithTitle() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        showTitleBar(false)
    }

    override fun initData() {
        editTextPhone.setText(SharePreUtils.getName(mContext))
        button.setOnClickListener {
          goLogin()
        }
    }

    fun goLogin(){
        showDialog()
        RetrofitUtils.INSTANCE.getRxjavaClient(AppService::class.java)
                .rxLogin("https://www.jsnh.xyz:3001/getusersign",editTextPhone.text.toString())
                .compose(RxSchedulers.io_main())
                .compose(RxSchedulers.handleResultTemp())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this as LifecycleOwner?)))
                .subscribe(object : CommonSubscriberTemp<LoginBean>(){
                    override fun onSuccess(response: LoginBean) {
                        dismissDialog()
                        SharePreUtils.setName(mContext, response.userID)
                        SharePreUtils.setjwt_token(mContext, response.sig)
                        SharePreUtils.setMemberId(mContext, response.sdkAppID.toString())
                        UiGoto.startAty(mContext, MainActivity::class.java)
                    }

                    override fun onFail(code: String?, message: String?) {
                        dismissDialog()
                    }
                });


    }
}