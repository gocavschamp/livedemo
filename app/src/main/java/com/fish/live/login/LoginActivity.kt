package com.fish.live.login

import android.os.Bundle
import com.fish.live.R
import com.fish.live.home.MainActivity
import com.nucarf.base.ui.BaseActivityWithTitle
import com.nucarf.base.utils.UiGoto
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivityWithTitle() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        showTitleBar(true)
    }

    override fun initData() {

        button.setOnClickListener { UiGoto.startAty(mContext, MainActivity::class.java) }
    }
}