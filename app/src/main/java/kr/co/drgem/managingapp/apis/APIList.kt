package kr.co.drgem.managingapp.apis

import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail
import retrofit2.Call
import retrofit2.http.*

interface APIList {

//    @FormUrlEncoded
//    @POST("/tablet/login")
//    fun postRequestLogin(
//        @Field("requesttype") requesttype: String = "01001", // 굳이 첨부 안해도 기본값 01001
//        @Field("sawoncode") email: String,
//        @Field("password") pw: String,    // 서버에 던질때 암호화 해서 던져야함
//    ) : Call<BasicResponse>
//
//    @GET("tablet/tran/detail/request")
//    fun getRequestTranDetail(
//        @Query("requesttype") requesttype : String,
//        @Query("georaemyeongsebeonho") georaemyeongsebeonho : String
//    ) : Call<BasicResponse>
//
//    @FormUrlEncoded
//    @POST("tablet/tran/detail/register")
//    fun postRequestTranDetail(
//        @Field("requesttype") requesttype : String,
//        @Field("georaemyeongsebeonho") georaemyeongsebeonho : String,
//        @Field("ipgosaupjangcode") ipgosaupjangcode : String,
//        @Field("ipgochanggocode") ipgochanggocode : String,
//        @Field("pummokcount") pummokcount : String,
//        @Field("georaedetail") georaedetail : Array<Georaedetail>   // 확인
//    ) : Call<BasicResponse>
//
//    @GET("tablet/vendorinfo/request")
//    fun getRequestVendorInfo (
//        @Query("requesttype") requesttype : String,
//        @Query("georaecheomyeong") georaecheomyeong : String,
//        @Query("baljubeonho") baljubeonho : String,
//    ) : Call<BasicResponse>
//
//    @GET("tablet/order/number/request")
//    fun getRequestOrderNumber()
//
//    @GET("tablet/order/detail/request")
//    fun getRequestOrderDetail()
//
//    @FormUrlEncoded
//    @POST("tablet/order/receive/register")
//    fun postRequestOrderReceive()
//
//    @GET("tablet/kitting/number/request")
//    fun getRequesKittingNumber
//
//    @GET("tablet/kitting/detail/request")
//
//    @FormUrlEncoded
//    @POST("tablet/delivery/batch/register")
//
//
//    @GET("tablet/request/number/request")
//
//    @GET("tablet/request/detail/request")
//
//    @FormUrlEncoded
//    @POST("tablet/request/delivery/register")
//
//    @GET("tablet/not-delivery/detail/request")
//
//    @FormUrlEncoded
//    @POST("tablet/not-delivery/delivery/register")
//
//    @GET("tablet/location/request")
//
//    @FormUrlEncoded
//    @POST("tablet/location/register")
//
//    @GET("tablet/productinfo/request")
//
//    @FormUrlEncoded
//    @POST("tablet/stock/register")
//
//    @GET("tablet/masterdata/request")
//
//    @GET("tablet/messagedata/request")
//    fun getRequestMessageData()
//
//

}