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
    val baljudetail: ArrayList<Baljudetail>? ,
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

    fun getUpPummokcode(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortBy { it.pummokcode }
        return sortList
    }

    fun getDownPummokcode(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortByDescending { it.pummokcode }
        return sortList
    }

    fun getUpPummyeong(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortBy { it.pummyeong }
        return sortList
    }

    fun getDownPummyeong(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortByDescending { it.pummyeong }
        return sortList
    }
    fun getUpDobeonModel(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortBy { it.dobeon_model }
        return sortList
    }

    fun getDownDobeonModel(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortByDescending { it.dobeon_model }
        return sortList
    }

    fun getUpSayang(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortBy { it.sayang }
        return sortList
    }

    fun getDownSayang(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortByDescending { it.sayang }
        return sortList
    }

    fun getUpLocation(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortBy { it.location }
        return sortList
    }

    fun getDownLocation(): ArrayList<Baljudetail>{
        val sortList : ArrayList<Baljudetail> = arrayListOf()
        sortList.clear()
        if (baljudetail != null) {
            sortList.addAll(baljudetail)
        }
        sortList.sortByDescending { it.location }
        return sortList
    }

}