package kr.co.drgem.managingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetailResponse(
    val georaecheocode: String?,
    val bigo: String?,
    val resultcd: String?,
    val requesttype: String?,
    val baljubeonho: String?,
    val nappumcheo: String?,
    val georaecheomyeong: String?,
    val baljuil: String?,
    val baljudetail: ArrayList<Baljudetail>?,
    val nappumjangso: String?,
    val resultmsg: String?
){

    @PrimaryKey(autoGenerate = true) var id: Int = 0
    fun getGeoraecheocodeHP() : String {
        if(georaecheocode == null){
            return "-"
        }
        return georaecheocode
    }

    fun getBigoHP() : String {
        if(bigo == null){
            return "-"
        }
        return bigo
    }


    fun getGeoraecheomyeongHP() : String {
        if(georaecheomyeong == null){
            return "-"
        }
        return georaecheomyeong
    }

    fun getBaljuilHP() : String {
        if(baljuil == null){
            return "-"
        }
        return baljuil
    }

    fun returnBaljudetail() : ArrayList<Baljudetail> {

        val baljuDetailList = ArrayList<Baljudetail>()

        if(baljudetail != null){
            baljuDetailList.clear()
            baljuDetailList.addAll(baljudetail)
        }

        return baljuDetailList
    }

    fun getNappumjangsoHP() : String {
        if(nappumjangso == null){
            return "-"
        }
        return nappumjangso
    }



}