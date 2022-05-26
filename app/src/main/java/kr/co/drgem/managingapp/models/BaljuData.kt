package kr.co.drgem.managingapp.models

import kr.co.drgem.managingapp.roomdb.datas.BaljuRoomData

data class BaljuData(
    val seq: String,
    val pummokcode: String,
    val pummyeong: String,
    val dobeon_model: String,
    val saying: String,
    val balhudanwi: String,
    val baljusuryang: String,
    val ipgoyejeongil: String,
    val giipgosuryang: String,
    val jungyojajeyeobu: String,
    val location : String,
) {
    fun toBaljuRoomData(baljubeonho: String) = BaljuRoomData(
        baljubeonho,
        this.seq,
        this.pummokcode,
        this.pummyeong,
        this.dobeon_model,
        this.saying,
        this.balhudanwi,
        this.baljusuryang,
        this.ipgoyejeongil,
        this.giipgosuryang,
        this.jungyojajeyeobu,
        this.location,
    )
}