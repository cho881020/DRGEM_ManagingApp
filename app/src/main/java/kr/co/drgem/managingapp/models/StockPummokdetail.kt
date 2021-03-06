package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject

data class StockPummokdetail(
    val pummokcode: String,
    val suryang: String,
    val josasigan : String,
    val location : String
){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()

        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("suryang", suryang)
        jsonObj.addProperty("josasigan", josasigan)
        jsonObj.addProperty("location", location)

        return jsonObj
    }
}