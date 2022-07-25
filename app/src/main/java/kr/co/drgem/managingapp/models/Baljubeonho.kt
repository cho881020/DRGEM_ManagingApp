package kr.co.drgem.managingapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Baljubeonho(
//    val requesttype: String?,         // 사용되지 않는다. 이름으로 받아와서 관계가 없는지 모르겠으나, 앞으로는 다른업무에서는 지울 것
//    val baljuiljastart: String?,      // 사용되지 않는다. 이름으로 받아와서 관계가 없는지 모르겠으나, 앞으로는 다른업무에서는 지울 것
//    val baljuiljaend: String?,        // 사용되지 않는다. 이름으로 받아와서 관계가 없는지 모르겠으나, 앞으로는 다른업무에서는 지울 것
    val georaecheomyeong: String?,   // 거래처명.
    val baljubeonho: String?,        // 발주번호
    val georaecheocode: String?,     // 거래처코드
    val baljuil: String?,            // 발주일
    val nappumjangso: String?,       // 납품장소
    val bigo: String?,               // 비고
) {
    fun getGeoraecheomyeongHP() : String {
        if(georaecheomyeong == null){
            return "-"
        }
        return georaecheomyeong
    }

    fun getBaljubeonhoHP() : String {
        if(baljubeonho == null){
            return "-"
        }
        return baljubeonho
    }

    fun getGeoraecheocodeHP() : String {
        if(georaecheocode == null){
            return "-"
        }
        return georaecheocode
    }

    fun getBaljuilHP() : String {
        if(baljuil == null){
            return "-"
        }
        return baljuil
    }

    fun getNappumjangsoHP() : String {
        if(nappumjangso == null){
            return "-"
        }
        return nappumjangso
    }

    fun getbigoHP() : String {
        if(bigo == null){
            return "-"
        }
        return bigo
    }
}