package kr.co.drgem.managingapp.models

data class RequestDetailResponse(
    val johoejogeon: String,
    val migwanri: String,
    val pummokcount: String,
    val pummokdetail: ArrayList<Pummokdetail>?,
    val requesttype: String,
    val resultcd: String,
    val resultmsg: String,
    val yocheongbeonho: String,
    val yocheongchanggocode: String,
    val yocheongsaupjangcode: String
){

    fun returnPummokDetail() : ArrayList<Pummokdetail> {

        val pummokdetailList = ArrayList<Pummokdetail>()

        if(pummokdetail != null){
            pummokdetailList.clear()
            pummokdetailList.addAll(pummokdetail)
        }

        return pummokdetailList
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

}