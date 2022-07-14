package kr.co.drgem.managingapp.models

data class KittingDetailResponse(
    val johoejogeon: String,
    val kittingbeonho: String,
    val migwanri: String,
    val pummokcount: String?,
    val pummokdetail: ArrayList<Pummokdetail>?,
    val requesttype: String,
    val resultcd: String,
    val resultmsg: String,
    val yochengchanggocode: String,
    val yochengsaupjangcode: String
){
    fun getPummokCount() : String {
        if(pummokcount == null){
            return "-"
        }
        return pummokcount
    }

    fun returnKittingDetail() : ArrayList<Pummokdetail> {

        val pummokdetailList = ArrayList<Pummokdetail>()

        if(pummokdetail != null){
            pummokdetailList.clear()
            pummokdetailList.addAll(pummokdetail)
        }

        return pummokdetailList
    }

    fun getUpPummokcode(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.pummokcode }
        return sortList
    }

    fun getDownPummokcode(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.pummokcode }
        return sortList
    }

    fun getUpPummyeong(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.pummyeong }
        return sortList
    }

    fun getDownPummyeong(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.pummyeong }
        return sortList
    }

    fun getUpDobeonModel(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.dobeon_model }
        return sortList
    }

    fun getDownDobeonModel(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.dobeon_model }
        return sortList
    }

    fun getUpSayang(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.sayang }
        return sortList
    }

    fun getDownSayang(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.sayang }
        return sortList
    }

    fun getUpLocation(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.location }
        return sortList
    }

    fun getDownLocation(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.location }
        return sortList
    }

    fun getUpYocheongBeonho(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.yocheongbeonho }
        return sortList
    }

    fun getDownYocheongBeonho(): ArrayList<Pummokdetail>{
        val sortList : ArrayList<Pummokdetail> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.yocheongbeonho }
        return sortList
    }

}