package com.nucarf.base.retrofit.logiclayer;


import android.util.Log;

import com.nucarf.base.R;
import com.nucarf.base.retrofit.LoginEvent;
import com.nucarf.base.retrofit.RetrofitConfig;
import com.nucarf.base.retrofit.RxSchedulers;
import com.nucarf.base.utils.BaseAppCache;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 */
public class BaseResult<T> {

    private String errorCode;
    private String errorMsg;
    private String error;
    private String code;
    private String errno;//好看视频error code  0成功
    private T result;
    private T data;
    private Object post;
    private Object message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Object getPost() {
        return post;
    }

    public void setPost(Object post) {
        this.post = post;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessed() {
        try {
            if (RetrofitConfig.STATUS_NCARF_SUCCESS.equals(errorCode) || "0".equals(errno)||"200".equals(code)) {
                if (getMessage() instanceof MessageBean) {
                    //第一次成功登陆时返回数据
                    MessageBean messageBean = (MessageBean) getMessage();
                    SharePreUtils.setIsNewUser(BaseAppCache.getContext(), !messageBean.getNew_user().equals("0"));
                }
                return true;
            } else if (errorCode.equals(RetrofitConfig.STATUS_GOTOLOGIN)) {
                SharePreUtils.setjwt_token(BaseAppCache.getContext(), "");
                EventBus.getDefault().post(new LoginEvent());
                return false;
            } else if (errorCode.equals(RetrofitConfig.STATUS_NO_EXITS)) {
                return false;
            } else if (errorCode.equals(RetrofitConfig.STATUS_COMPANY_OR_ID_ERROR)) {
                return false;
            } else {
                ToastUtils.show_middle_pic(R.mipmap.ic_launcher, getMessage() instanceof String ? getMessage() + "" : "", 0);
                Log.d(RxSchedulers.TAG, errorMsg + "");
                if (errorCode.equals("1")) {
                    if (getMessage() instanceof String) {
                        String message = (String) getMessage();
                        if (message.equals("token_invalid")) {
                            LogUtils.e("base result" + "token_invalid");
                            SharePreUtils.setjwt_token(BaseAppCache.getContext(), "");
                            EventBus.getDefault().post(new LoginEvent());
                        }
                    }
                }
                return false;
            }
        } catch (Exception e) {
            ToastUtils.show_middle_pic(R.mipmap.ic_launcher, "网络错误", 0);
            Log.d(RxSchedulers.TAG, "  " + e);

            return false;
        }
    }


}
