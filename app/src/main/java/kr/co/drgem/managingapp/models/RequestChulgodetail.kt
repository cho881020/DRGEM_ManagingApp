package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject

data class RequestChulgodetail(
    val pummokcode: String,
    val chulgosuryang: String,
    val jungyojajeyeobu: String,
    val serialnumber: String
){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()
        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("chulgosuryang", chulgosuryang)
        jsonObj.addProperty("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.addProperty("serialnumber", serialnumber)

        return jsonObj
    }
}