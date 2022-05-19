package kr.co.drgem.managingapp.apis

import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @FormUrlEncoded
    @POST("/tablet/login")
    fun postRequestLogin(
        @Field("requesttype") requesttype: String = "01001", // 굳이 첨부 안해도 기본값 01001
        @Field("sawoncode") email: String,
        @Field("password") pw: String,    // 서버에 던질때 암호화 해서 던져야함
    ) : Call<BasicResponse>

    @GET("tablet/tran/detail/request") //거래명세요청
    fun getRequestTranDetail(
        @Query("requesttype") requesttype : String = "02001",
        @Query("georaemyeongsebeonho") georaemyeongsebeonho : String
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("tablet/tran/detail/register")//거래명세등록
    fun postRequestTranDetail(
        @Field("requesttype") requesttype : String = "02002",
        @Field("georaemyeongsebeonho") georaemyeongsebeonho : String,
        @Field("ipgosaupjangcode") ipgosaupjangcode : String,
        @Field("ipgochanggocode") ipgochanggocode : String,
        @Field("pummokcount") pummokcount : String,
        @Field("georaedetail") georaedetail : Array<Georaedetail>   // 확인
    ) : Call<BasicResponse>

    @GET("tablet/vendorinfo/request") // 거래처요청(삭제예정)
    fun getRequestVendorInfo (
        @Query("requesttype") requesttype : String = "02011",
        @Query("georaecheomyeong") georaecheomyeong : String,
        @Query("baljubeonho") baljubeonho : String,
    ) : Call<BasicResponse>

    @GET("tablet/order/number/request")
    fun getRequestOrderNumber(
        @Query("requesttype") requesttype: String = "02012",
        @Query("georaecheocode") georaecheocode: String,
        @Query("baljuiljastart") baljuiljastart: String,
        @Query("baljuiljaend") baljuiljaend: String,

    )

    @GET("tablet/order/detail/request")
    fun getRequestOrderDetail(

    )

    @FormUrlEncoded
    @POST("tablet/order/receive/register")
    fun postRequestOrderReceive()

    @GET("tablet/kitting/number/request")
    fun getRequestKittingNumber()

    @GET("tablet/kitting/detail/request")
    fun getRequestKittingDetail()

    @FormUrlEncoded
    @POST("tablet/delivery/batch/register")
    fun postRequestDeliveryBatch()


    @GET("tablet/request/number/request")
    fun getRequestRequestNumber()

    @GET("tablet/request/detail/request")
    fun getRequestRequestDetail()

    @FormUrlEncoded
    @POST("tablet/request/delivery/register")
    fun postRequestRequestDelivery()

    @GET("tablet/not-delivery/detail/request")
    fun getRequestNotDeliveryDetail()

    @FormUrlEncoded
    @POST("tablet/not-delivery/delivery/register")
    fun postRequestNotDeliveryDelivery()

    @GET("tablet/location/request")
    fun getRequestLocation()

    @FormUrlEncoded
    @POST("tablet/location/register")
    fun postRequestLocation()

    @GET("tablet/productinfo/request")
    fun getRequestProductinfo()

    @FormUrlEncoded
    @POST("tablet/stock/register")
    fun postRequestStock()

    @GET("tablet/masterdata/request")
    fun getRequestMasterdata()

    @GET("tablet/messagedata/request")
    fun getRequestMessageData()


}