package com.nucarf.base.retrofit.api;

/**
 * Creator: mrni-mac on 16-menu_code_no_pressed-5.
 * Email  : kakaluote.com
 */
public interface TestHttp {

    /**
     * Creator: kakaluote.
     * Email  : kakaluote.com
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * menu_code_no_pressed.使用(@QueryMap@Path 谁都可以用)
     * <p/>
     * post
     * <p/>
     * 表单post
     *
     * @FormUrlEncoded
     * @POST("/users") void createUser(@Field("user") User user, Callback<User> cb);
     * <p/>
     * 表单post
     * @FormUrlEncoded
     * @POST("/users") void createUser(@FieldMap Map<String, String> options, Callback<User> cb);
     * <p/>
     * 文件post
     * @Multipart
     * @POST("/user/addLicenseInfo") void addLicenseInfo(@QueryMap Map<String, Object> options, @Part("file") TypedFile file, Callback<JsonElement> response);
     * <p/>
     * new TypedFile("application/octet-stream”,File)
     * <p/>
     * <p/>
     * get
     * @GET("/group/{id}/users") List<User> groupList(@Path("id") int groupId, @Query("sort") String sort);
     * @GET("/group/{id}/users") List<User> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
     * <p/>
     * <p/>
     * <p/>
     * FirstHttp firstHttp = RetrofitUtils.INSTANCE.getClient(FirstHttp.class);
     * firstHttp.sendRequest(BaseHttp.getBaseParams(), "Index", "home", "114655", new HttpCallBack<BaseResult<HomePageBean>>() {
     * @Override public void succeeded(BaseResult<HomePageBean> homePageBeanBaseResult, Response response) {
     * HomePageBean homePageBean = homePageBeanBaseResult.getInfo();
     * }
     * @Override public void failed(String msg) {
     * <p/>
     * }
     * });
     *
     *
     */
//    //get请求
//    @GET("auth/login")
//    Call<BaseResult<LoginBean>> testget(@QueryMap Map<String, String> baseParam, @Query("mobile") String mobile, @Query("password") String password);
//    //post请求
//    @FormUrlEncoded
//    @POST("auth/login")
//    Call<BaseResult<LoginBean>> authlogin(@FieldMap Map<String, String> baseParam, @Field("mobile") String mobile, @Field("password") String password);

}
