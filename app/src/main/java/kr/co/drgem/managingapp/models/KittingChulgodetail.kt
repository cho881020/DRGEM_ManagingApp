package kr.co.drgem.managingapp.models

import com.google.gson.JsonObject

data class KittingChulgodetail(
    val yocheongbeonho: String,
    val pummokcode: String,
    val georaesuryang: String,
    val jungyojajeyeobu: String,
    val serialnumber: String,

){
    fun toJsonObject() : JsonObject {       // JSONObject 로 제작하는
        val jsonObj = JsonObject()
        jsonObj.addProperty("yocheongbeonho", yocheongbeonho)
        jsonObj.addProperty("pummokcode", pummokcode)
        jsonObj.addProperty("chulgosuryang", georaesuryang)
        jsonObj.addProperty("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.addProperty("serialnumber", serialnumber)

        return jsonObj
    }

}