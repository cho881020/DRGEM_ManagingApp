package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject
import org.json.JSONObject

data class GeoraedetailAdd(
    val seq: String,
    val pummokcode: String,
    val georaesuryang: String,
    val jungyojajeyeobu: String,
    val baljuheonho: String,
    val serialnumber: String
){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()
        jsonObj.addProperty("seq", seq)
        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("georaesuryang", georaesuryang)
        jsonObj.addProperty("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.addProperty("baljuheonho", baljuheonho)
        jsonObj.addProperty("serialnumber", serialnumber)

        return jsonObj
    }
}