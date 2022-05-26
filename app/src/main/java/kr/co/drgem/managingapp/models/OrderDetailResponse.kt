package kr.co.drgem.managingapp.models

data class OrderDetailResponse(
    val baljubeonho: String,
    val baljudetail: ArrayList<Baljudetail>,
    val baljuil: String,
    val bigo: String,
    val georaecheocode: String,
    val georaecheomyeong: String,
    val nappumjangso: String,
    val resultcd: String,
    val resultmsg: String
)