package kr.co.drgem.managingapp.apis

import android.location.Location
import kr.co.drgem.managingapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @POST("/tablet/login")
    fun postRequestLogin(
        @Body params: HashMap<String, String>,
//        @Field("requesttype") requesttype: String = "01001", // 굳이 첨부 안해도 기본값 01001
//        @Field("sawoncode") email: String,
//        @Field("password") pw: String,    // 서버에 던질때 암호화 해서 던져야함
    ) : Call<BasicResponse>
//

    @GET("tablet/master-data/request")
    fun getRequestMasterData(
        @Query("requesttype") requesttype : String = "09001",
        @Query("mastertype") mastertype : String = "all"
    ) : Call<MasterDataResponse>


    @GET("tablet/tran/detail/request")
    fun getRequestTranDetail(
        @Query("requesttype") requesttype : String = "02001",
        @Query("georaemyeongsebeonho") georaemyeongsebeonho : String
    ) : Call<TranResponse>

    @FormUrlEncoded
    @POST("tablet/tran/detail/register")
    fun postRequestTranDetail(
        @Field("requesttype") requesttype : String = "02002",
        @Field("georaemyeongsebeonho") georaemyeongsebeonho : String,
        @Field("ipgosaupjangcode") ipgosaupjangcode : String,
        @Field("ipgochanggocode") ipgochanggocode : String,
        @Field("pummokcount") pummokcount : String,
        @Field("georaedetail") georaedetail : Array<Georaedetail>   // 확인
    ) : Call<BasicResponse>

//    @GET("tablet/vendorinfo/request")
//    fun getRequestVendorInfo (
//        @Query("requesttype") requesttype : String = "02011",
//        @Query("georaecheomyeong") georaecheomyeong : String,
//        @Query("baljubeonho") baljubeonho : String,
//    ) : Call<BasicResponse>

    @GET("tablet/order/number/request")
    fun getRequestOrderNumber(
        @Query("requesttype") requesttype: String = "02011",
        @Query("baljuiljastart") baljuiljastart: String,
        @Query("baljuiljaend") baljuiljaend: String,
        @Query("georaecheomyeong") georaecheomyeong: String,
        @Query("baljubeonho") baljubeonho: String,
    ) : Call<OrderResponse>

    @GET("tablet/order/detail/request")
    fun getRequestOrderDetail(
    @Query("requesttype") requesttype: String,
    @Query("baljubeonho") baljubeonho: String,
    ) : Call<OrderDetailResponse>

    @FormUrlEncoded
    @POST("tablet/order/receive/register")
    fun postRequestOrderReceive()

    @GET("tablet/kitting/number/request")
    fun getRequestKittingNumber(
        @Query("requesttype") requesttype : String = "02051",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("kittingja") kittingja : String,
        @Query("changgocode") changgocode : String,
    ) : Call<KittingResponse>

    @GET("tablet/kitting/detail/request")
    fun getRequestKittingDetail(
        @Query("requesttype") requesttype : String = "02502",
        @Query("kittingbeonho") kittingbeonho : String,
        @Query("johoejogeon") johoejogeon : String,
        @Query("migwanri") migwanri : String,
        @Query("changgocode") changgocode : String,
    ) : Call <KittingDetailResponse>


    @FormUrlEncoded
    @POST("tablet/delivery/batch/register")
    fun postRequestDeliveryBatch()


    @GET("tablet/request/number/request")
    fun getRequestRequestNumber(
        @Query("requesttype") requesttype : String = "02061",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("saupjang") saupjang : String,
        @Query("yocheonchanggocode") yocheonchanggocode : String,
        @Query("yocheongja") yocheongja : String,
    ) : Call<RequestResponse>

    @GET("tablet/request/detail/request")
    fun getRequestRequestDetail(
        @Query("requesttype") requesttype : String = "02062",
        @Query("yocheongbeonho") yocheongbeonho : String,
        @Query("johoejogeon") johoejogeon : String,
        @Query("migwanri") migwanri : String,
        @Query("saupjangcode") saupjangcode : String,
        @Query("yocheonchanggocode") yocheonchanggocode : String,
    ) :Call<RequestDetailResponse>

    @FormUrlEncoded
    @POST("tablet/request/delivery/register")
    fun postRequestRequestDelivery()

    @GET("tablet/not-delivery/detail/request")
    fun getRequestNotDeliveryDetail(
        @Query("requesttype") requesttype : String = "02071",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("saupjang") saupjang : String,
        @Query("yocheongchanggocode") yocheonchanggocode : String,
        @Query("yocheongja") yocheongja : String,
        @Query("yocheongpummok") yocheongpummok : String,
        @Query("migwanri") migwanri : String,
    ) :Call<NotDeliveryResponse>

    @FormUrlEncoded
    @POST("tablet/not-delivery/delivery/register")
    fun postRequestNotDeliveryDelivery()

    @GET("tablet/location/request")
    fun getRequestLocation(
        @Query("requesttype") requesttype : String = "02081",
        @Query("changgocode") changgocode : String,
        @Query("location") location : String,
        @Query("pummyeong") pummyeong : String,
    ) :Call<LocationResponse>

    @FormUrlEncoded
    @POST("tablet/location/register")
    fun postRequestLocation()

    @GET("tablet/product-info/request")
    fun getRequestProductinfo(
        @Query("requesttype") requesttype : String = "02091",
        @Query("pummokcode") pummokcode : String,
    ):Call<ProductInfoResponse>

    @FormUrlEncoded
    @POST("tablet/stock/register")
    fun postRequestStock()

    @GET("tablet/masterdata/request")
    fun getRequestMasterdata()

    @GET("tablet/messagedata/request")
    fun getRequestMessageData()


}