package kr.co.drgem.managingapp.models

data class MasterSawonResponse(

    val sawoncount: String,
    val resultcd: String,
    val resultmsg: String,
    val sawon: ArrayList<SawonData>,
)