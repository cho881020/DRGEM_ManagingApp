package kr.co.drgem.managingapp.models

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

    fun getResultcdHP() : String {
        if(resultcd == null){
            return "-"
        }
        return resultcd
    }

    fun getRequesttypeHP() : String {
        if(requesttype == null){
            return "-"
        }
        return requesttype
    }

    fun getBaljubeonhoHP() : String {
        if(baljubeonho == null){
            return "-"
        }
        return baljubeonho
    }

    fun getNappumcheoHP() : String {
        if(nappumcheo == null){
            return "-"
        }
        return nappumcheo
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

    fun getResultmsgHP() : String {
        if(resultmsg == null){
            return "-"
        }
        return resultmsg
    }



}