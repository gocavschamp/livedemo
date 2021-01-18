package com.nucarf.base.retrofit;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.nucarf.base.R;
import com.nucarf.base.mvp.BaseView;
import com.nucarf.base.retrofit.logiclayer.BaseResult;
import com.nucarf.base.utils.BaseAppCache;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by codeest on 2017/2/23.
 */

public abstract class CommonSubscriberTemp<T> implements Observer<T> {
    /**
     * 对应HTTP的状态码
     */
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int CONFLICT = 409;
    private static final int PRECONDITION_FAILED = 412;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * 服务器定义的状态吗
     * 比如：登录过期，提醒用户重新登录；
     *      添加商品，但是服务端发现库存不足，这个时候接口请求成功，服务端定义业务层失败，服务端给出提示语，客户端进行吐司
     *      请求接口，参数异常或者类型错误，请求code为200成功状态，不过给出提示，这个时候客户端用log打印服务端给出的提示语，方便快递查找问题
     *      其他情况，接口请求成功，但是服务端定义业务层需要吐司服务端返回的对应提示语
     */
    /**
     * 完全成功
     */
    private static final int CODE_SUCCESS = 0;
    /**
     * Token 失效
     */
    public static final int CODE_TOKEN_INVALID = 401;
    /**
     * 缺少参数
     */
    public static final int CODE_NO_MISSING_PARAMETER = 400400;
    /**
     * 其他情况
     */
    public static final int CODE_NO_OTHER = 403;
    /**
     * 统一提示
     */
    public static final int CODE_SHOW_TOAST = 400000;
    /**
     * //token 失效
     */
    private static final int TOKEN_INVALID = 400;


    private BaseView mView;
    private String mErrorMsg;
    private boolean isShowErrorState = false;

    protected CommonSubscriberTemp() {

    }

//    protected CommonSubscriberTemp(BaseView view, String errorMsg) {
//        this.mView = view;
//        this.mErrorMsg = errorMsg;
//    }
//
//    protected CommonSubscriberTemp(BaseView view, boolean isShowErrorState) {
//        this.mView = view;
//        this.isShowErrorState = isShowErrorState;
//    }
//
//    protected CommonSubscriberTemp(BaseView view, String errorMsg, boolean isShowErrorState) {
//        this.mView = view;
//        this.mErrorMsg = errorMsg;
//        this.isShowErrorState = isShowErrorState;
//    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    /**
     * 服务器返回数据，但响应码不为200
     */
    abstract public void onFail(String code, String message);

    @Override
    public void onError(Throwable e) {
        Log.d("TAG", "error:: " + e);
        BaseResult ex = new BaseResult();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex.setCode(httpException.code() + "");
            if (httpException.code() == UNAUTHORIZED || httpException.code() == FORBIDDEN || httpException.code() == TOKEN_INVALID) {
                ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "您的账号已在其他设备登录", 0);
                SharePreUtils.setjwt_token(BaseAppCache.getContext(), "");
//                SharePreUtils.removeKey(BaseAppCache.getContext());
                EventBus.getDefault().post(new LoginEvent());
            } else if (((HttpException) e).code() == NOT_FOUND) {
                ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "找不到指定的链接!", 0);
            } else {
                ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "网络错误!", 0);
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "解析错误!", 0);

        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "请求超时!", 0);

        } else if (e instanceof ConnectException) {
            ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "连接服务器失败,请检查网络!", 0);

        } else if (e instanceof SocketException) {
            ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "网络中断，请检查网络!", 0);

        } else if (e instanceof UnknownHostException) {
            ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, "连接服务器失败,请稍候重试", 0);

        } else {
            if (!e.getMessage().contains("301")) {
                ToastUtils.show_middle_pic(R.mipmap.icon_toast_error, e.getMessage() + "", 0);
            }
        }
        onFail(ex.getCode(), e.getMessage());
    }
}
