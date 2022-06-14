package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject
import org.json.JSONObject

data class IpgodetaildetailAdd(

    val seq: String,
    val pummokcode: String,
    val georaesuryang: String,
    val jungyojajeyeobu: String,
    val serialnumber: String
) {
    fun toJsonObject() : JsonObject {
        val jsonObj = JsonObject()
        jsonObj.addProperty("seq", seq)
        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("ipgosuryang", georaesuryang)
        jsonObj.addProperty("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.addProperty("serialnumber", serialnumber)

        return jsonObj
    }
}