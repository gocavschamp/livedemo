package com.nucarf.base.retrofit;

/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 */
public class RetrofitConfig {

    public static final String ERROR_NETWORK = "net_error";
    public static final String ERROR_PARSE = "parse_error";
    public static final String STATUS_SUCCESS = "200";
    public static final String STATUS_NCARF_SUCCESS = "0";
    public static final String STATUS_NCARF_ERROR = "1";
    //授权过期，需要登录
    public static final String STATUS_GOTOLOGIN = "100";
    public static final String STATUS_NO_EXITS = "404";
    //领取时候 公司或者身份证不一致
    public static final String STATUS_COMPANY_OR_ID_ERROR = "301";


    public final static int HTTP_CONNECT_TIMEOUT = 1000 * 10;
    public final static int HTTP_READ_TIMEOUT = 1000 * 10;
    public final static int HTTP_WRITE_TIMEOUT = 1000 * 10;
    public final static int RESPONSE_CACHE_SIZE = 1024 * 1024 * 12;

    //服务器
    //0 正式1 测试 2 预发布 3 演示
//    switch (index) {
//        case 0:
//            $api.setStorage('LoginString', 'vB8SAqViGxYtUomFZpfF3NnQAe43Kt');
//            $api.setStorage('BaseUrl', 'https://api.nucarf.com/');
//            $api.setStorage('wxUrl', 'https://wechat.nucarf.com');
//            $api.setStorage('picAjaxUrl', 'https://wechat.nucarf.com');
//            break;
//        case 1:
//            $api.setStorage('LoginString', '880e96ce5cda3ea607bf11184825ce6d');
//            $api.setStorage('BaseUrl', 'http://tapi.nucarf.cn/');
//            $api.setStorage('wxUrl', 'http://twechat.nucarf.cn');
//            $api.setStorage('picAjaxUrl', 'http://twechat.nucarf.cn');
//            break;
//        case 2:
//            $api.setStorage('LoginString', '880e96ce5cda3ea607bf11184825ce6d');
//            $api.setStorage('BaseUrl', 'http://rapi.nucarf.cn/');
//            $api.setStorage('wxUrl', 'http://rwechat.nucarf.cn');
//            $api.setStorage('picAjaxUrl', 'http://rwechat.nucarf.cn');
//            break;
//        case 3:
//            $api.setStorage('LoginString', '880e96ce5cda3ea607bf11184825ce6d');
//            $api.setStorage('BaseUrl', 'http://api.nucarf.cn/');
//            $api.setStorage('wxUrl', 'http://wechat.nucarf.cn');
//            $api.setStorage('picAjaxUrl', 'http://wechat.nucarf.cn');
//            break;
//    };

    //正式 http://api.startvshow.com/
//    public static final String TEST_HOST_URL = "http://tapi.nucarf.cn/V1/";
    public static final String TEST_HOST_URL = "http://2019_0418_invoice_new.api.yidian.nucarf.cn/V1/";
    public static final String TEST_WAN_ANDROID = "https://www.wanandroid.com/";//玩安卓api
    /**
     * 正式：'https://wechat.nucarf.com';
     */
    public static final String TEST_WECHAT_URL = "http://twechat.nucarf.cn";
    public static final String WECHAT_URL = "https://wechat.nucarf.com";
//    public static final String TEST_HOST_URL = AppUtils.getApplicationInfoMateData(BaseAppCache.getContext(), "TEST_HOST_URL");

    public static final String TEMP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/starshow/";


    //    public static final boolean isTest = AppUtils.getApplicationInfoMateDataBoolean(BaseAppCache.getContext(), "APP_IS_TEST");
    public static final boolean isTest =true;
    public static final String CODE_LOGIN_STR = isTest ? "2dd5183143e74b1a" : "2dd5183143e74b1a";
    public static final String PWD_LOGIN_STR = isTest ? "880e96ce5cda3ea607bf11184825ce6d" : "vB8SAqViGxYtUomFZpfF3NnQAe43Kt";
    //极光推送 app_key e516f2ff191ecad388c53974
    public static final String CODE_JPUSH_STR = "e516f2ff191ecad388c539";

    /**
     //微信 apiSecret 995f6ee44a0b28ef026f0bfca0069c97
     *
     微信 appid wx8d519b866f73fcbc
     */
    public static final String WX_APPID = "yyywx8d519b866f73fcbc";
    public static final String WX_APISECRET = "yyy995f6ee44a0b28ef026f0bfca00697";

    //友盟 AppKey：
    //5c19c610b465f56e03000259
    public static final String UM_APPKEY = "5c19c610b465f56e0300";

}
