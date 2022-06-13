package kr.co.drgem.managingapp.models

data class StockAdd(
    val requesttype: String,
    val changgocode: String,
    val location: String,
    val seq: String,
    val status: String,
    val pummokcount: String,
    val pummokdetail: ArrayList<StockPummokdetail>,
)