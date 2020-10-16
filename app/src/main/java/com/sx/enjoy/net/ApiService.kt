package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.WebDataBean
import com.sx.enjoy.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //注册发送验证码
    @GET("api-user/user/code")
    fun sendCode(@QueryMap map:Map<String,String>):Observable<HttpResult<String>>

    //注册
    @POST("api-user/user/register")
    fun register(@Body body : RequestBody):Observable<HttpResult<String>>

    //登录
    @POST("api-user/user/login")
    fun login(@Body body : RequestBody):Observable<HttpResult<UserBean>>

    //忘记密码
    @POST("api-user/user/fgPassword")
    fun forgetPassword(@Body body : RequestBody):Observable<HttpResult<String>>

    //查询用户信息
    @GET("api-user/user/findUser")
    fun getUserInfo(@Query("id")id:String):Observable<HttpResult<UserBean>>

    //修改用户
    @POST("api-user/user/changeUser")
    fun updateUserInfo(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改用户
    @POST("api-user/user/updatePhone")
    fun updateUserPhone(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改登录密码
    @POST("api-user/user/upPassword")
    fun updateLoginPassword(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改支付密码
    @POST("api-user/user/setPayPassword")
    fun updatePayPassword(@Body body : RequestBody):Observable<HttpResult<String>>


    //是否签到
    @GET("api-question/signRecord/judgmentSign")
    fun getSignResult(@Query("userId")userId:String):Observable<HttpResult<Boolean>>

    //题库
    @GET("api-question/findQuestionsRandom")
    fun getQuestionList():Observable<HttpResult<List<QuestionBean>>>

    //签到
    @POST("api-question/signRecord/addSign")
    fun userSign(@Body body : RequestBody):Observable<HttpResult<String>>

    //任务卷轴
    @GET("api-task/taskmanager")
    fun getTaskList():Observable<HttpResult<List<TaskListBean>>>

    //我的任务卷轴
    @GET("api-task/taskorder/status")
    fun getMyTaskList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<TaskListBean>>>

    //签到
    @POST("api-task/taskmanager/buyScroll")
    fun buyTask(@Body body : RequestBody):Observable<HttpResult<String>>

    //我的任务卷轴
    @GET("api-task/taskorder/getTaskRiceGrains")
    fun getTaskRiceGrains(@Query("userId")userId:String):Observable<HttpResult<TaskRiceBean>>

    //米粒明细
    @GET("api-user/rich-detail/getRichDetail")
    fun getRiceRecordList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<RiceRecordListBean>>>

    //商城分类
    @GET("api-mall/category/findAllFirstAndSecond")
    fun getStoreCategory(@Query("pid")pid:String):Observable<HttpResult<List<StoreCategoryBean>>>

    //文件上传
    @POST("api-file/files-anon")
    fun uploadFile(@Body body: RequestBody): Call<HttpResult<UploadImageBean>>

    //我购买的/我售卖的
    @GET("api-rich/rich-order")
    fun getMyMarketOrderList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<MarketTransactionListBean>>>

    //步行模式计算距离
    @POST("api-home/travelmode/walkCompute")
    fun getRiceFromStep(@Body body: RequestBody):Observable<HttpResult<StepRiceBean>>

    //发布买入/卖出
    @POST("api-rich/rich")
    fun publishMarketInfo(@Body body: RequestBody):Observable<HttpResult<String>>

    //市场列表
    @GET("api-rich/rich")
    fun getMarketList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<MarketListBean>>>

    //市场行情
    @GET("api-rich/rich-quotes")
    fun getMarketQuotes(@QueryMap map:Map<String,String>):Observable<HttpResult<List<MarketQuotesBean>>>

    //买入/卖出详情
    @GET("api-rich/rich/id")
    fun getMarketDetails(@Query("id")id:String):Observable<HttpResult<MarketListBean>>

    //米粒订单创建
    @POST("api-rich/rich-order")
    fun createMarketOrder(@Body body: RequestBody):Observable<HttpResult<String>>

    //买入/卖出订单详情
    @GET("api-rich/rich-order/id")
    fun getTransactionOrderDetails(@Query("id")id:String):Observable<HttpResult<TransactionOrderBean>>

    //米粒订单支付
    @POST("api-rich/rich-order/pay")
    fun payMarketOrder(@Body body: RequestBody):Observable<HttpResult<String>>

    //米粒订单确认
    @POST("api-rich/rich-order/confirm")
    fun confirmMarketOrder(@Body body: RequestBody):Observable<HttpResult<String>>

    //推荐团队
    @GET("api-user/user/getUserTeam")
    fun getUserTeamList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<TeamListBean>>>

    //历史步数
    @GET("api-home/travelmode/findHistoryByModel")
    fun getWalkHistory(@QueryMap map:Map<String,String>):Observable<HttpResult<List<WalkHistoryBean>>>

    //Banner
    @GET("api-system/sxbanner/getBanner")
    fun getHomeBanner():Observable<HttpResult<BannerListBean>>

    //通知
    @GET("api-system/sxnotice/getAll")
    fun getHomeNotice():Observable<HttpResult<List<NoticeListBean>>>

    //新闻列表
    @GET("api-system/sxcontentmanager")
    fun getHomeNews(@QueryMap map:Map<String,String>):Observable<HttpResult<List<NewsListBean>>>

    //商品列表
    @GET("api-mall/commodity/findGoodsByCondition")
    fun getCommodityList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<CommodityListBean>>>

    //商品详情
    @GET("api-mall/commodity/{id}")
    fun getCommodityDetails(@Path("id")id:String):Observable<HttpResult<CommodityDetailsBean>>

    //猜你喜欢
    @GET("api-mall/commodity/getGuessYouLike")
    fun getCommodityLikeList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<CommodityListBean>>>

    //购物车数量
    @GET("api-mall/shoppingcar/getShoppingCarCount")
    fun getShopCartCount(@Query("userId")userId:String):Observable<HttpResult<String>>

    //加入购物车
    @POST("api-mall/shoppingcar/addShoppingCar")
    fun addShopCart(@Body body: RequestBody):Observable<HttpResult<String>>

    //创建订单
    @POST("api-mall/mallorder/generateOrder")
    fun createOrder(@Body body: RequestBody):Observable<HttpResult<NewOrderBean>>

    //获取第一个收货地址
    @GET("api-mall/receiveraddress/findAddressFirst")
    fun getFirstAddress(@Query("userId")userId:String):Observable<HttpResult<AddressBean>>

    //保存收货地址
    @POST("api-mall/receiveraddress")
    fun saveAddress(@Body body: RequestBody):Observable<HttpResult<String>>

    //收货地址
    @GET("api-mall/receiveraddress/findAddressByUserId")
    fun getMyAddressList(@Query("userId")userId:String):Observable<HttpResult<List<AddressBean>>>

    //购物车
    @GET("api-mall/shoppingcar/getShoppingCar")
    fun getMyShopCart(@Query("userId")userId:String):Observable<HttpResult<List<ShopCartBean>>>

    //添加购物商品数量
    @POST("api-mall/shoppingcar/addGoodsNumber")
    fun addCommodityNumber(@Body body: RequestBody):Observable<HttpResult<String>>

    //添加购物商品数量
    @POST("api-mall/shoppingcar/deleteByIds")
    fun deleteCommodityFromShopCart(@Body body: RequestBody):Observable<HttpResult<String>>

    //获取订单状态数量
    @GET("api-mall/mallorder/findOrderStatus")
    fun getMyOrderStatusCount(@Query("userId")userId:String):Observable<HttpResult<OrderStatusCountBean>>

    //订单列表
    @GET("api-mall/mallorder/findMineOrder")
    fun getOrderList(@QueryMap map:Map<String,String>):Observable<HttpResult<List<OrderListBean>>>

    //取消订单
    @POST("api-mall/mallorder/cancelOrder")
    fun cancelOrder(@Body body: RequestBody):Observable<HttpResult<String>>

    //订单详情
    @GET("api-mall/mallorder/findOrderDetail")
    fun getOrderDetails(@Query("orderNo")orderNo:String):Observable<HttpResult<OrderDetailsBean>>

    //删除订单
    @POST("api-mall/mallorder/deleteOrder")
    fun deleteOrder(@Body body: RequestBody):Observable<HttpResult<String>>

    //实名认证
    @POST("api-user/authrecord")
    fun authUser(@Body body: RequestBody):Observable<HttpResult<PayResultBean>>

    //实名认证查询
    @GET("api-user/authrecord/getAuthByUserId")
    fun getAuthUser(@Query("userId")userId:String):Observable<HttpResult<AuthUserBean>>

    //实名认证查询
    @POST("api-pay/order/pay")
    fun orderPay(@Body body: RequestBody):Observable<HttpResult<PayResultBean>>

    //获取通知详情
    @GET("api-system/sxnotice/id")
    fun getNoticeDetails(@Query("id")id:String):Observable<HttpResult<NoticeDetailsBean>>

    //实名认证查询
    @POST("api-system/sxfeedback")
    fun userFeedback(@Body body: RequestBody):Observable<HttpResult<String>>

    //获取文本信息
    @GET("api-system/sxcontentmanager/getContentMangerByType")
    fun getWebData(@Query("type")type:String):Observable<HttpResult<WebDataBean>>

    //会员升级
    @POST("api-system/vipconfig/vipUpgrade")
    fun memberUp(@Body body: RequestBody):Observable<HttpResult<String>>

    //用户等级信息
    @GET("api-system/vipconfig/getFutureLevel")
    fun getMemberInfo(@Query("userId")userId:String):Observable<HttpResult<MemberUpBean>>

    //新闻详情
    @GET("api-system/sxcontentmanager/id")
    fun getNewsDetails(@Query("id")id:String):Observable<HttpResult<NewsDetailsBean>>

}
