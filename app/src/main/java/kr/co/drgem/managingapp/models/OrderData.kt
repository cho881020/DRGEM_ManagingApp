package kr.co.drgem.managingapp.models

class OrderData(

    val resultcd: String,
    val resultmsg: String,
    val baljubeonho: String,
    val baljuil: String,
    val georaecheocode: String,
    val georaecheomyeong: String,
    val nappumcheo: String,
    val nappumcheomyeong: String,
    val pummokcount: String,
    val baljudetail: List<BaljuData>,
) {
}