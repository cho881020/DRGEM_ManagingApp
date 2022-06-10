package kr.co.drgem.managingapp.models

data class LocationAdd(
    val pummokcount: String,
    val pummokdetail: List<LocationPummokdetail>,
    val requesttype: String,
    val seq: String,
    val status: String
)