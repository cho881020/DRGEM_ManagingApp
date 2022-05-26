package kr.co.drgem.managingapp.roomdb.datas

import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.co.drgem.managingapp.models.BaljuData

@Entity
data class BaljuRoomData(
    var baljubeonho: String,
    var seq: String,
    var pummokcode: String,
    var pummyeong: String,
    var dobeon_model: String,
    var saying: String,
    var balhudanwi: String,
    var baljusuryang: String,
    var ipgoyejeongil: String,
    var giipgosuryang: String,
    var jungyojajeyeobu: String,
    var location : String,
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    fun toBaljuData() = BaljuData(
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