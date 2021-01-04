package com.nucarf.base.retrofit.api;

/**
 * Created by WANG on 2016/4/27.
 */
public interface UserServices {
//    @GET("user/home")
//    Call<BaseResult<UserBean>> userhome(@QueryMap Map<String, String> baseParam, @Query("jwt_token") String jwt_token);
//
//    @FormUrlEncoded
//    @POST("user/update")
//    Call<BaseResult<UserBean>> update(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("head_pic") String head_pic, @Field("name") String name, @Field("introduction") String introduction, @Field("email") String email, @Field("phone") String phone, @Field("wechat_id") String wechat_id, @Field("job") String job, @Field("company") String company);
//
//    @FormUrlEncoded
//    @POST("user/update")
//    Call<BaseResult<UserBean>> update_head(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("head_pic") String head_pic);
//    @FormUrlEncoded
//    @POST("user/update")
//    Call<BaseResult<UserBean>> update_card(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("job") String job, @Field("phone") String phone, @Field("email") String email);
//
//    @FormUrlEncoded
//    @POST("user/update")
//    Call<BaseResult<UserBean>> update_wechatid(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("wechat_id") String wechat_id);
//
//    //关注列表
//    @GET("user/{id}/follows")
//    Call<BaseResult<UserTotal>> follows(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    //粉丝列表
//    @GET("user/{id}/followers")
//    Call<BaseResult<UserTotal>> followers(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    //关注
//    @FormUrlEncoded
//    @POST("user/{id}/follow")
//    Call<BaseResult<FollowStatus>> follow(@Path("id") String id, @FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token);
//
//    //他人中心
//    @GET("user/{id}")
//    Call<BaseResult<UserBean>> othercenter(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("jwt_token") String jwt_token);
//
//    @FormUrlEncoded
//    @POST("user/changeBackground")
//    Call<BaseResult<String>> changeBackground(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("background_pic") String background_pic);
//
//    @GET("user/{id}/posts")
//    Call<BaseResult<ListBean>> posts(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("count") String count, @Query("jwt_token") String jwt_token);
//
//    //个人文章
//    @GET("user/{id}/article")
//    Call<BaseResult<NewsBeanTotal>> news(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    //个人直播
//    @GET("user/{id}/lives")
//    Call<BaseResult<CenterLiveToatal>> lives(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    //个人视频
//    @GET("user/{id}/videos")
//    Call<BaseResult<CenterLiveToatal>> videos(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    @GET("user/{id}/exchange")
//    Call<BaseResult<OrderTotal>> order(@Path("id") String id, @QueryMap Map<String, String> baseParam, @Query("current_count") String current_count, @Query("jwt_token") String jwt_token);
//
//    //意见反馈
//    @FormUrlEncoded
//    @POST("user/feedback")
//    Call<BaseResult<String>> feedback(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("content") String content, @Field("mobile") String mobile);
//
//    @GET("user/action")
//    Call<BaseResult> getUserPermission(@QueryMap Map<String, String> baseParam, @Query("jwt_token") String jwt_token);
//
//    //作品类型列表
//    @GET("user/workTypes")
//    Call<BaseResult<ArrayList<WorksType>>> workTypes(@QueryMap Map<String, String> baseParam, @Query("jwt_token") String jwt_token);
//
//
//    @FormUrlEncoded
//    @POST("user/storeWorks")
//    Call<BaseResult<StoreWorks>> storeWorks(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("title") String title, @Field("work_type_id") String work_type_id, @Field("publish_time") String publish_time, @Field("cover") String cover);
//
//    @FormUrlEncoded
//    @POST("user/deleteWorks")
//    Call<BaseResult<String>> deleteWorks(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("work_id") String work_id);
//
//    @GET("user/{userId}/workList")
//    Call<BaseResult<WorksTotal>> workList(@Path("userId") String userId, @QueryMap Map<String, String> baseParam, @Query("jwt_token") String jwt_token, @Query("current_count") String current_count);
//
//    @FormUrlEncoded
//    @POST("user/updateWorks")
//    Call<BaseResult<StoreWorks>> updateWorks(@FieldMap Map<String, String> baseParam, @Field("jwt_token") String jwt_token, @Field("work_id") String work_id, @Field("title") String title, @Field("work_type_id") String work_type_id, @Field("publish_time") String publish_time, @Field("cover") String cover);
}
