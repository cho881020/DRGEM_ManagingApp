package kr.co.drgem.managingapp.apis

import kr.co.drgem.managingapp.models.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIList {

    @FormUrlEncoded
    @POST("/pda/login")
    fun postRequestLogin(
        @Field("requesttype") requesttype: String = "01001", // 굳이 첨부 안해도 기본값 01001
        @Field("sawoncode") email: String,
        @Field("password") pw: String,    // 서버에 던질때 암호화 해서 던져야함
    ) : Call<BasicResponse>

}