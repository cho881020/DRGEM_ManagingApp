package kr.co.drgem.managingapp.models

import org.json.JSONObject

data class IpgodetaildetailAdd(

    val seq: String,
    val pummokcode: String,
    val georaesuryang: String,
    val jungyojajeyeobu: String,
    val serialnumber: String
) {
    fun toJsonObject() : JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put("seq", seq)
        jsonObj.put("pummokcode", pummokcode)
        jsonObj.put("ipgosuryang", georaesuryang)
        jsonObj.put("jungyojajeyeobu", jungyojajeyeobu)
        jsonObj.put("serialnumber", serialnumber)

        return jsonObj
    }
}