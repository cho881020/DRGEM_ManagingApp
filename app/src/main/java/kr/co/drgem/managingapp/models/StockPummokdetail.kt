package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject

data class StockPummokdetail(
    val pummokcode: String,
    val suryang: String
){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()

        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("suryang", suryang)

        return jsonObj
    }
}