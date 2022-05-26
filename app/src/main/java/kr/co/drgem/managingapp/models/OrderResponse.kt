package kr.co.drgem.managingapp.models

data class OrderResponse(
    val baljubeonho: ArrayList<Baljubeonho>,
    val resultcd: String,
    val resultmsg: String
)