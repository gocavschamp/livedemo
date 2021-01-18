package com.fish.live.service;


import com.fish.live.bean.LoginBean;
import com.nucarf.base.retrofit.logiclayer.BaseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface AppService {
//    @FormUrlEncoded
//    @POST("Sms/sendCaptcha")
//    Call<BaseResult<Object>> getCode(@QueryMap Map<String, String> baseParam, @Field("mobile") String mobile, @Field("deviceId") String deviceId);
//
//    @FormUrlEncoded
//    @POST("Member/login")
//    Call<BaseResult<StringBean>> login(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    @FormUrlEncoded
//    @POST("Member/checkCode")
//    Call<BaseResult<Object>> checkCode(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    @FormUrlEncoded
//    @POST("Member/appRegister")
//    Call<BaseResult<Object>> appRegister(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    @FormUrlEncoded
//    @POST("Member/login")
//    Call<BaseResult<StringBean>> loginByMsg(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    @FormUrlEncoded
//
//    /**
//     * 取消收藏
//     * //Member/cancleCollectStationsOrLines
//     * type	 1 油站 2 路线
//     * sa_id	342
//     */
//    @FormUrlEncoded
//    @POST("Member/cancleCollectStationsOrLines")
//    Call<BaseResult<Object>> cancleCollectStationsOrLines(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 路线是否收藏
//     * /Member/isCollectLine
//     */
//    @FormUrlEncoded
//    @POST("Member/isCollectLine")
//    Observable<BaseResult<StringBean>> isCollectLine(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * //Road/map
//     * start:108.93977,34.341574
//     * end:116.407394,39.904211
//     * location:'113.625328,34.746611';'112.938884,28.22808'
//     * only_petrol:0
//     */
//    @FormUrlEncoded
//    @POST("Road/data")
//    Call<BaseResult<SearchWayBean>> roadMap(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * /getStationInfoByidsNew
//     * order_id 0 默认 1价格 2距离
//     * fuel_no
//     */
//    @FormUrlEncoded
//    @POST("Station/getStationInfoByids")
//    Call<BaseResult<StationListBean>> getStationInfoByidsNew(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * MemberCart/showTaCode
//     */
//    @FormUrlEncoded
//
//    /**
//     * 获取待领取油款
//     * getRechargeTmp
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/getRechargeTmp")
//    Observable<BaseResult<ArrayList<CardBean>>> rxGetRechargeTmp(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
    /**
     * 获取待领取油款
     * getRechargeTmp
     */
    @FormUrlEncoded
    @POST("Member/getRechargeTmpList")
//    @POST("http://driver_3.0.0.api.tyidian.nucarf.cn/v3/Member/getRechargeTmpList")
    Observable<BaseResult<ArrayList<String>>> rxGetWaitRechargeList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
    /**
     * 获取待领取油款
     * getRechargeTmp
     */
    @GET
    Observable<BaseResult<LoginBean>> rxLogin(@Url String url, @Query("userID") String userID);

//    /**
//     * 领取油款
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/getCompanyMoeny")
//    Call<BaseResult<Object>> getCompanyMoeny(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 领取油款
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/getCompanyMoeny")
//    Observable<BaseResult<Object>> rxGetCompanyMoeny(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消费记录接口
//     * fuel_no
//     * time_day
//     * limit	20
//     * page	1
//     * isMore	false
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Member/orderList")
////    @POST("http://yapi.nucarf.cn/mock/26/V3/Member/orderList")
//    Call<BaseResult<CostListBean>> getStationOrder(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 清除消费记录接口
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Member/blankStationOrder")
//    Call<BaseResult<Object>> blankStationOrder(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 清除储值记录接口
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Recharge/blankRechargeList")
//    Call<BaseResult<Object>> blankRechargeList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 清除转油记录接口
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/clearTransfersRecord")
//    Call<BaseResult<Object>> clearTransfersRecord(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 违章查询
//     * wzQuery
//     */
//    @FormUrlEncoded
//    @POST("Member/wzQuery")
//    Observable<BaseResult<QueryViolationBean>> rxQueryVialation(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 司机付款账户列表  新加接口
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/subOrderList")
////    @POST("http://yapi.nucarf.cn/mock/26/V3/Member/subOrderList")
//    Call<BaseResult<SubOrderListBean>> subOrderList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消费记录详情
//     * order_id	468311
//     * user_type	1
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Member/orderInfo")
//    Call<BaseResult<CostDetailsBean>> getOrderInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 圈存凭证
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/orderInfo")
//    Observable<BaseResult<CostDetailsBean>> rxGetOrderInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 评价消费记录
//     * order_id	474875
//     * level	5
//     * comment	真好啊
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Comment/setOrderComment")
//    Call<BaseResult<Object>> setOrderComment(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 查看评价
//     * order_id	474875
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Comment/getOrderComment")
//    Call<BaseResult<CommentOrderBean>> getOrderComment(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消费记录上传照片
//     * order_id	150816
//     * images[0]	https://twechat.nucarf.cn/Public/Uploads/tmp/20190423/5cbeb2da8a38e.png
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("OrderImages/uploadOrderImage")
//    Call<BaseResult<Object>> uploadOrderImage(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 储值记录接口
//     * fuel_no
//     * time_day
//     * month
//     * limit	20
//     * page	1
//     * isMore	false
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Recharge/getRechargeList")
//    Call<BaseResult<ChargeRecordBean>> getRechargeList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * MemberCart/getCartList
//     */
//    @FormUrlEncoded
//    @POST("Member/getOrderConf")
//    Call<BaseResult<ArrayList<CardConfigBean>>> getCardConfig(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * MemberCart/getZzAccount
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/getZzAccount")
//    Call<BaseResult<ZzAccountBean>> getZzAccount(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * MemberCart/taMember
//     * 获取对象信息
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/taMember")
//    Call<BaseResult<ZzAccountBean>> getUserInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 转油
//     * MemberCart/memberTa
//     * mc_id	11310 ---个人账户卡号
//     * mobile	18131877607
//     * number
//     * name	吴晓伟
//     * money	10
//     * source	1 --个人 2-企业
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/memberTa")
//    Call<BaseResult<Object>> giveOilToMember(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 参数：number	3493557739
//     * token	e8e155a57e2756b48ab6939b80c35e504847d8a8
//     */
//    @FormUrlEncoded
//    @POST("Company/byNumberGetName")
//    Call<BaseResult<StringBean>> checkCompany(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取加油二维码
//     * pay_pass	96387e17017142a7572d4e89d7a15ff0870a131a
//     * pay_pass=7a490bcd5ecedc16eb57b0581f505b0f9dc04751
//     * cart_id	33408
//     * lat	39.910067
//     * long	116.470146
//     * is_location	0
//     * secs	58
//     * token	43b47b029cae59a4b60df69cedfcc575d1dffa6d
//     */
//    @FormUrlEncoded
//    @POST("Pay/getPayCode")
////    @POST("http://yapi.nucarf.cn/mock/26/v3/pay/getPayCode")
//    Call<BaseResult<StringBean>> getPayCode(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取转账记录列表
//     * limit  返回数据条数默认100条
//     * offset	116.470146
//     * mc_id	司机卡号
//     * month	 2019-03
//     * token	43b47b029cae59a4b60df69cedfcc575d1dffa6d
//     */
//    @FormUrlEncoded
//    @POST("MemberCart/getTransfersRecord")
//    Call<BaseResult<ExchangeRecordBean>> getTransfersRecord(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 推荐油站
//     * name	北京海淀开放路加油站
//     * address	北京市海淀区开放路
//     * mobile	17600467122
//     * reason	常去,油价便宜,出入方便,服务好
//     * long	116.470146
//     * lat	39.910067
//     * openid
//     * images[0]	https://twechat.nucarf.cn/Public/Uploads/tmp/20190402/5ca2b5c4179cb.jpg
//     * images[1]	https://twechat.nucarf.cn/Public/Uploads/tmp/20190402/5ca2b5c8c46df.jpg
//     * images[2]	https://twechat.nucarf.cn/Public/Uploads/tmp/20190402/5ca2b5cfc190d.jpg
//     * images[3]	https://twechat.nucarf.cn/Public/Uploads/tmp/20190402/5ca2b5d5ac4bf.jpg
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     */
//    @FormUrlEncoded
//    @POST("StationCollect/createData")
//    Call<BaseResult<Object>> recommentStation(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 上传图片
//     * twechat.nucarf.cn /Image/uploadSearchForm
//     *
//     * @return
//     */
//    @Headers({"Domain-Name: uploadpic"})
//    @FormUrlEncoded
//    @POST
//    Call<BaseResult<String>> uploadSearchForm(@Url String url, @QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 推荐注册
//     * /Member/registerMember
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/registerMember")
//    Call<BaseResult<String>> registerMember(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取油站信息
//     * /Station/getStationInfo
//     * si_id
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getStationInfo")
//    Call<BaseResult<StationInfoBean>> getStationInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取油品列表
//     * /Station/getStationFuel
//     * si_id
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getStationFuel")
//    Call<BaseResult<ArrayList<FuelsBean>>> getStationFuel(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * /Order/byPayKeyCreateOrder
//     * si_id
//     * fuel_no	2001
//     * mc_id	11284
//     * order_type	1
//     * registrationId	190e35f7e005d48bdb3
//     * si_id	132
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * total_money	100
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("pay/memberCreateOrder")
//    Call<BaseResult<StringBean>> byPayKeyCreateOrder(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取油站搜素条件
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/getFuelsBrandAndOthers")
//    Call<BaseResult<StationLimitBean>> getFuelsBrandAndOthers(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取评论列表
//     * si_id	64
//     * limit	20
//     * page	1
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getSic")
//    Call<BaseResult<StationCommentListBean>> getStationComment(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取收藏油站列表
//     * long	64
//     * lat
//     * limit	20
//     * page	1
//     * isMore	1
//     * distance	1
//     * show_goods	1
//     * status	1
//     * user_type	1
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/getCollectStationList")
//    Call<BaseResult<StationListBean>> getCollectStationList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取收藏路线列表
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/getCollectLinesList")
//    Call<BaseResult<List<LinesListBean>>> getCollectLinesList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取所有的油站点并可筛选
//     * only_petrol:0
//     * member_id:1124
//     * h_ids:
//     * area_code:
//     * fuel_no:
//     * station_brand:
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getPointsList")
//    Call<BaseResult<List<NewPointsBean>>> getPointsList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取所有高速
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getHighList")
//    Call<BaseResult<List<HighWayBean>>> getHighList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * App版本更新
//     * type 1 ios 2 安卓
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("AppVersion/checkVersion")
//    Call<BaseResult<CheckUpdateBean>> checkVersion(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取优惠活动(司机端)
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberDiscount/getDiscountList")
//    Call<BaseResult<DisCountBean>> getDiscountList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 获取优惠适用油站
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberDiscount/getDiscountInfo")
//    Call<BaseResult<StationListBean>> getDiscountInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消息列表
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberNews/getNewList")
//    Call<BaseResult<MessageTotalBean>> getNewList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消息已读接口
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberNews/readStatus")
//    Call<BaseResult<Object>> readStatus(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消息列表
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberNews/getNewInfo")
//    Call<BaseResult<MsgListBean>> getNewInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 消息通知 公告列表
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Notice/getNotice")
////    @POST("http://yapi.nucarf.cn/mock/26/v3/Notice/getNotice")
//    Call<BaseResult<NoticeBean>> getNotice(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 推荐油站
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("StationCollect/getList")
//    Call<BaseResult<StationListBean>> getRecommendStationList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 设置司机常用油品
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("MemberFuel/setFuel")
//    Call<BaseResult<Object>> setFuel(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 根据司机当前位置获取油站的提示语音
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("road/getAudio")
//    Call<BaseResult<AudioBean>> getAudio(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 根据司机当前位置获取油站的提示语音
//     * token	45b875c3c419980b1347cacddf67dc1edeef22c9
//     * deviceId	864797040033413
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Member/settingHeadImg")
//    Call<BaseResult<StringBean>> settingHeadImg(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 创建和悦二维码
//     * param
//     * mc_id	T文本	是	 卡id
//     * si_id	T文本	是	 油站id
//     * amount	T文本	是	 金额
//     * fuel_no	T文本	是	 油品
//     *
//     * @return code
//     * 1001 	余额不足或金额超出限制
//     * 1002 	系统错误
//     * 1003 	非法参数
//     * 1004 	系统余额不足
//     * 1050 	未知错误
//     */
//    @FormUrlEncoded
//    @POST("Heyue/createQrcode")
//    Call<BaseResult<HeYueBean>> createHeYueQrcode(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    @FormUrlEncoded
//    @POST("Heyue/createQrcode")
//    Observable<BaseResult<HeYueBean>> rxCreateHeYueQrcode(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 车享创建订单
//     * token	T文本	是
//     * si_id	T文本	是
//     * mc_id	T文本	是
//     * fuel_no	T文本	是
//     * amount	T文本	是
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("pay/memberCreateOrder")
//    Observable<BaseResult<StringBean>> rxCreateOrderChexiang(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * token	T文本	是
//     * si_id	T文本	是
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Station/getCartNo")
//    Observable<BaseResult<StringBean>> rxGetCartNo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 外协司机申请回收油款列表
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("member/backMoneyList")
//    Observable<BaseResult<List<RecelyMoneyBean>>> rxbackMoneyList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 外协司机审核回收油款
//     * id	T文本	是
//     * status	T文本	1 同意 2 不同意
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("member/backMoney")
//    Observable<BaseResult<StringBean>> rxbackMoney(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 和悦退卡接口
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Heyue/cancel")
//    Call<BaseResult> destoryOrder(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 和悦卡查询状态接口
//     *
//     * @param baseParam
//     * @param request
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("Heyue/getStatus")
//    Call<BaseResult<HeYueStatusBean>> getHeYueOrderStatus(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    /**
//     * 位置上传登录  (http://shenguan.ci.nucarf.cn/)
//     * channel	string
//     * licenseNumber	string
//     * name	string
//     * phoneNumber	string
//     * token	string
//     * uid	string
//     *
//     * @param request
//     * @return
//     */
//    @Headers({"Domain-Name: lbslocation"})
//    @FormUrlEncoded
//    @POST
//    Call<LbsBean> lbsLogin(@Url String url, @FieldMap Map<String, String> request);
//
//    /**
//     * 实时上报位置信息 (http://open.ci.nucarf.cn/)
//     * channel	string
//     * lat	integer($int64)
//     * lon	integer($int64)
//     * token	string
//     * updateTime	integer($int64)
//     *
//     * @param request
//     * @return
//     */
//    @Headers({"Domain-Name: lbslocation"})
//    @FormUrlEncoded
//    @POST
//    Call<LbsBean> uploadVehiclePosition(@Url String url, @FieldMap Map<String, String> request);
//
//    /**
//     * 消息记录-修改油站
//     *
//     * @param map
//     */
//    @FormUrlEncoded
//    @POST("chexiang/changeStation")
//    Call<BaseResult<Object>> modifyStation(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> map);
//
//
//    /**
//     * 车享申请撤销
//     */
//    @FormUrlEncoded
//    @POST("Chexiang/applyCancel")
//    Call<BaseResult<Object>> applyCancel(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> map);
//
//
//    /**
//     * 申请撤销理由
//     */
//    @FormUrlEncoded
//    @POST("chexiang/cancelReasonList")
//    Call<BaseResult<List<ReasonBean>>> getCancelReasonList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> map);
//
//    /**
//     * 消费记录详情
//     * order_id	468311
//     * user_type	1
//     * token	78509862a8f472c8b3be69ae4d94c8692fd9d20f
//     */
//    @FormUrlEncoded
//    @POST("Order/getOrderInfo")
//    Observable<BaseResult<List<CostDetailsBean>>> getCostOrderInfo(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//    //附近油站列表
//    @FormUrlEncoded
//    @POST("Road/getNearbyStationList?")
//    Observable<BaseResult<StationListBean>> getRXNearbyStationList(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> request);
//
//
//    //取消申请
//    @FormUrlEncoded
//    @POST("Chexiang/closeApply")
//    Call<BaseResult<Object>> cancelApply(@QueryMap Map<String, String> baseParam, @FieldMap Map<String, String> map);
}
