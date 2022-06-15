package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject

data class LocationPummokdetail(
    val pummokcode: String,
    val location: String,
){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()
        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("location", location)
        return jsonObj
    }
}