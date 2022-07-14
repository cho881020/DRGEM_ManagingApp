package kr.co.drgem.managingapp.models

data class TranResponse (

    val georaecheocode :String?,
    val bigo : String?,
    val georaedetail : ArrayList<Georaedetail>?,
    val resultcd: String?,
    val requesttype: String?,
    val georaeil : String?,
    val nappumcheo : String?,
    val georaecheomyeong : String?,
    val resultmsg: String?,
    val nappumcheomyeong : String?,
    val pummokcount : String?,
    val georaemyeongsebeonho : String?,
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

    fun getGeoraeilHP() : String {
        if(georaeil == null){
            return "-"
        }
        return georaeil
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

    fun getNappumcheomyeongHP() : String {
        if(nappumcheomyeong == null){
            return "-"
        }
        return nappumcheomyeong
    }

    fun getPummokcountHP() : String {
        if(pummokcount == null){
            return "-"
        }
        return pummokcount
    }

    fun getGeoraemyeongsebeonhoHP() : String {
        if(georaemyeongsebeonho == null){
            return "-"
        }
        return georaemyeongsebeonho
    }

    fun returnGeoraedetail() : ArrayList<Georaedetail> {
        val GeoraeList = ArrayList<Georaedetail>()

        if(georaedetail != null){
            GeoraeList.clear()
            GeoraeList.addAll(georaedetail)
        }
        return GeoraeList
    }

    fun getUpSeq(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.seq?.toInt() }
        return sortList
    }

    fun getDownSeq(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.seq?.toInt() }
        return sortList
    }

    fun getUpPummokcode(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.pummokcode }
        return sortList
    }

    fun getDownPummokcode(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.pummokcode }
        return sortList
    }

    fun getUpPummyeong(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.pummyeong }
        return sortList
    }

    fun getDownPummyeong(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.pummyeong }
        return sortList
    }

    fun getUpDobeonModel(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.dobeon_model }
        return sortList
    }

    fun getDownDobeonModel(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.dobeon_model }
        return sortList
    }

    fun getUpSayang(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.sayang }
        return sortList
    }

    fun getDownSayang(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.sayang }
        return sortList
    }

    fun getUpLocation(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortBy { it.location }
        return sortList
    }

    fun getDownLocation(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        if (georaedetail != null) {
            sortList.addAll(georaedetail)
        }
        sortList.sortByDescending { it.location }
        return sortList
    }

}