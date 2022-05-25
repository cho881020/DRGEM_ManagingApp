package kr.co.drgem.managingapp.models

data class MasterDataResponse(
    val common: ArrayList<Common>,
    val commoncount: String,
    val resultcd: String,
    val resultmsg: String
)