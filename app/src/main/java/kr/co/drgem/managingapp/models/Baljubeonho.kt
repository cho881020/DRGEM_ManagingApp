package kr.co.drgem.managingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Baljubeonho(

    val requesttype: String?,
    val baljuiljastart: String?,
    val baljuiljaend: String?,
    val georaecheomyeong: String?,   //거래처명.
    val baljubeonho: String?,        //발주번호
    val georaecheocode: String?,     //거래처코드
    val baljuil: String?,            //발주일
    val nappumjangso: String?,        //납품장소
    val bigo: String?,               //비고


) {

    fun getGeoraecheomyeongHP() : String {
        if(georaecheomyeong == null || georaecheomyeong =="null"){
            return "-"
        }
        return georaecheomyeong
    }

    fun getBaljubeonhoHP() : String {
        if(baljubeonho == null|| baljubeonho =="null"){
            return "-"
        }
        return baljubeonho
    }

    fun getGeoraecheocodeHP() : String {
        if(georaecheocode == null|| georaecheocode =="null"){
            return "-"
        }
        return georaecheocode
    }

    fun getBaljuilHP() : String {
        if(baljuil == null|| baljuil =="null"){
            return "-"
        }
        return baljuil
    }

    fun getNappumjangsoHP() : String {
        if(nappumjangso == null|| nappumjangso =="null"){
            return "-"
        }
        return nappumjangso
    }

    fun getbigoHP() : String? {
        if(bigo == null|| bigo =="null"){
            return "-"
        }
        return bigo
    }


}