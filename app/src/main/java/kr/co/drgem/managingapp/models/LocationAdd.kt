package kr.co.drgem.managingapp.models

data class LocationAdd(
    val requesttype: String,
    val pummokcount: String,
    val seq: String,
    val status: String,
    val pummokdetail: List<LocationPummokdetail>,
)