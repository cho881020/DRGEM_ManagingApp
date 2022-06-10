package kr.co.drgem.managingapp.models

data class RequestAdd(

    val requesttype: String,
    val yocheongbeonho: String,
    val chulgoilja: String,

    val chulgosaupjangcode: String,
    val chulgochanggocode: String,
    val chulgodamdangjacode: String,

    val ipgosaupjangcode: String,
    val ipgochanggocode: String,
    val ipgodamdangjacode: String,

    val seq: String,
    val status: String,
    val pummokcount: String,
    val chulgodetail: ArrayList<RequestChulgodetail>,
)