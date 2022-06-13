package kr.co.drgem.managingapp.models

data class NotDeliveryAdd(

    val requesttype: String,
    val chulgoilja: String,
    val chulgosaupjangcode: String,
    val chulgochanggocode: String,
    val chulgodamdangjacode: String,
    val ipgosaupjangcode: String,
    val ipgodamdangjacode: String,
    val ipgochanggocode: String,
    val seq: String,
    val status: String,
    val pummokcount: String,
    val chulgodetail: List<NotDeliveryChulgodetail>,


)