package com.fish.live;

public class Constants {
    /**
     * 腾讯云直播拉流Demo提供的默认URL
     */
    public static final String NORMAL_PLAY_URL = "http://liteavapp.qcloud.com/live/liteavdemoplayerstreamid.flv";

    /**
     * 腾讯云直播拉流文档URL
     */
    public static final String LIVE_PLAYER_DOCUMENT_URL = "https://cloud.tencent.com/document/product/454/7886";

    /**
     * 腾讯云直播拉流超低时延播放文档URL
     */
    public static final String LIVE_PLAYER_REAL_TIME_PLAY_DOCUMENT_URL = "https://cloud.tencent.com/document/product/454/7886#RealTimePlay";

    /**
     * 超低时延测试RTMP URL
     */
    public static final String RTMP_ACC_TEST_URL = "https://lvb.qcloud.com/weapp/utils/get_test_rtmpaccurl";

    /**
     * MainActivity启动LivePlayerActivity时传递的Activity Type的KEY
     */
    public static final String INTENT_ACTIVITY_TYPE = "TYPE";

    /**
     * QRCodeScanActivity完成扫描后，传递过来的结果的KEY
     */
    public static final String INTENT_SCAN_RESULT = "SCAN_RESULT";

    /**
     * LivePlayerURLActivity设置页面传递给LivePlayerActivity的直播地址
     */
    public static final String INTENT_URL = "intent_url";

    public static final String URL_PREFIX_HTTP = "http://";
    public static final String URL_PREFIX_HTTPS = "https://";
    public static final String URL_PREFIX_RTMP = "rtmp://";
    public static final String URL_SUFFIX_FLV = ".flv";
    public static final String URL_TX_SECRET = "txSecret";
    public static final String URL_BIZID = "bizid";       //是否为低延迟拉流地址


    public static final int ACTIVITY_TYPE_LIVE_PLAY = 1;    // 标准直播播放
    public static final int ACTIVITY_TYPE_REALTIME_PLAY = 2;    // 低延时直播播放

    public static final float CACHE_TIME_FAST = 1.0f;
    public static final float CACHE_TIME_SMOOTH = 5.0f;

    public static final int CACHE_STRATEGY_FAST = 0;        //极速
    public static final int CACHE_STRATEGY_SMOOTH = 1;        //流畅
    public static final int CACHE_STRATEGY_AUTO = 2;        //自动

    public static final int PLAY_STATUS_SUCCESS = 0;
    public static final int PLAY_STATUS_EMPTY_URL = -1;
    public static final int PLAY_STATUS_INVALID_URL = -2;
    public static final int PLAY_STATUS_INVALID_PLAY_TYPE = -3;
    public static final int PLAY_STATUS_INVALID_RTMP_URL = -4;
    public static final int PLAY_STATUS_INVALID_SECRET_RTMP_URL = -5;


    // 上传常量
    public static final String PLAYER_DEFAULT_VIDEO = "play_default_video";
    public static final String PLAYER_VIDEO_ID = "video_id";

    //IM
    public static final int IM_APPID = 1400474693;//IM
    public static final String IM_KEY = "6a80c2ac4cfa4b60d597715794d532e0bc48dac132b2f6a47b47d93e93e5d178";//IM

    // 点播的信息
    public static final int VOD_APPID = 1304627676;//直播
    public static final String VOD_APPKEY = "1973fcc2b70445af8b51053d4f9022bb";

    public static final String SERVER_IP = "http://demo.vod2.myqcloud.com/shortvideo";
    public static final String ADDRESS_VIDEO_LIST = SERVER_IP + "/api/v1/resource/videos";

    public static final String RTMP_URL = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4";

    public static class RetCode {
        // 服务器返回码
        public static final int CODE_SUCCESS = 0;    // 接口请求成功
        public static final int CODE_PARAMS_ERR = 1001; // 请求参数错误
        public static final int CODE_AUTH_ERR = 1002; // 鉴权错误
        public static final int CODE_RES_ERR = 1003; // 资源不存在
        public static final int CODE_REQ_TOO_FAST_ERR = 1004; // 请求频率过快
        public static final int CODE_SERVER_ERR = 1000; // 服务器错误
        // 客户端处理码
        public static final int CODE_REQUEST_ERR = 1;    // 请求错误
        public static final int CODE_PARSE_ERR = 2;    // 解析json错误
    }
}
