package kr.co.drgem.managingapp.models

data class OrderDetailResponse(
    val georaecheocode: String,
    val bigo: String,
    val resultcd: String,
    val requesttype: String,
    val baljubeonho: String,
    val nappumcheo: String,
    val georaecheomyeong: String,
    val baljuil: String,
    val baljudetail: ArrayList<Baljudetail>,
    val nappumjangso: String,
    val resultmsg: String
)