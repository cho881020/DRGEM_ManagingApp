package kr.co.drgem.managingapp.models

import org.json.JSONObject

data class ChulgoDetailAdd(
    val yocheongbeonho: String,
    val pummokcode: String,
    val georaesuryang: String,
    val jungyojajeyeobu: String,
    val serialnumber: String,
){
    fun toJsonObject() : JSONObject {       // JSONObject 로 제작하는
        val jsonObj = JSONObject()
        jsonObj.put("yocheongbeonho", yocheongbeonho)
        jsonObj.put("pummokcode", pummokcode)
        jsonObj.put("georaesuryang", georaesuryang)
        jsonObj.put("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.put("serialnumber", serialnumber)

        return jsonObj
    }
}