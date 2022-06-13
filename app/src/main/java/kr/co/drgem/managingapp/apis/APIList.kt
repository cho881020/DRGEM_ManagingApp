package kr.co.drgem.managingapp.apis

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


    @POST("tablet/workseq/request")
    fun postRequestSEQ(
        @Body params: HashMap<String, String>,
) : Call<WorkResponse>

    @POST("tablet/workstatus/cancle")
    fun postRequestWorkstatusCancle(
        @Body params: HashMap<String, String>,
    ) : Call<WorkResponse>

    @POST("tablet/temp-extantstock/register")
    fun postRequestTempExtantstock(
        @Body params: HashMap<String, String>,
    ) : Call<WorkResponse>

    @GET("tablet/tran/detail/request")
    fun getRequestTranDetail(
        @Query("requesttype") requesttype : String = "02001",
        @Query("georaemyeongsebeonho") georaemyeongsebeonho : String
    ) : Call<TranResponse>


    @POST("tablet/tran/detail/register")
    fun postRequestTranDetail(
        @Body params: HashMap<String, String>,
    ) : Call<BasicResponse>

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

    @POST("tablet/order/receive/register")
    fun postRequestOrderReceive(
        @Body params: HashMap<String, String>,
    ) : Call<WorkResponse>

    @GET("tablet/kitting/number/request")
    fun getRequestKittingNumber(
        @Query("requesttype") requesttype : String = "02051",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("kittingja") kittingja : String,
        @Query("changgomyeong") changgocode : String,
    ) : Call<KittingResponse>

    @GET("tablet/kitting/detail/request")
    fun getRequestKittingDetail(
        @Query("requesttype") requesttype : String = "02502",
        @Query("kittingbeonho") kittingbeonho : String,
        @Query("johoejogeon") johoejogeon : String,
        @Query("migwanri") migwanri : String,
        @Query("changgocode") changgocode : String,
    ) : Call <KittingDetailResponse>


    @POST("tablet/delivery/batch/register")
    fun postRequestDeliveryBatch(
        @Body params: KittingAdd
    ) : Call<WorkResponse>


    @GET("tablet/request/number/request")
    fun getRequestRequestNumber(
        @Query("requesttype") requesttype : String = "02061",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("saupjangcode") saupjang : String,
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


    @POST("tablet/request/delivery/register")
    fun postRequestRequestDelivery(
        @Body params: RequestAdd
    ) : Call<WorkResponse>

    @GET("tablet/not-delivery/detail/request")
    fun getRequestNotDeliveryDetail(
        @Query("requesttype") requesttype : String = "02071",
        @Query("sijakilja") sijakilja : String,
        @Query("jongryoilja") jongryoilja : String,
        @Query("saupjangcode") saupjang : String,
        @Query("yocheongchanggocode") yocheonchanggocode : String,
        @Query("yocheongja") yocheongja : String,
        @Query("yocheongpummok") yocheongpummok : String,
        @Query("migwanri") migwanri : String,
    ) :Call<NotDeliveryResponse>


    @POST("tablet/not-delivery/delivery/register")
    fun postRequestNotDeliveryDelivery(
        @Body params: NotDeliveryAdd
    ) : Call<WorkResponse>

    @GET("tablet/location/request")
    fun getRequestLocation(
        @Query("requesttype") requesttype : String = "02081",
        @Query("changgocode") changgocode : String,
        @Query("location") location : String,
        @Query("pummyeong") pummyeong : String,
    ) :Call<LocationResponse>

    @POST("tablet/location/register")
    fun postRequestLocation(
        @Body params: LocationAdd
    ) : Call<WorkResponse>

    @GET("tablet/product-info/request")
    fun getRequestProductinfo(
        @Query("requesttype") requesttype : String = "02091",
        @Query("pummokcode") pummokcode : String,
    ):Call<ProductInfoResponse>


    @POST("tablet/stock/register")
    fun postRequestStock()

    @GET("tablet/masterdata/request")
    fun getRequestMasterdata()

    @GET("tablet/messagedata/request")
    fun getRequestMessageData()


}