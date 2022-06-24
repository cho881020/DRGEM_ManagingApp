package kr.co.drgem.managingapp.models

data class LocationResponse(
    val pummokcount: String,
    val pummokdetail: ArrayList<Pummokdetail>?,
    val resultcd: String,
    val resultmsg: String
){

    fun returnPummokDetail() : ArrayList<Pummokdetail> {

        val pummokdetailList = ArrayList<Pummokdetail>()

        if(pummokdetail != null){
            pummokdetailList.clear()
            pummokdetailList.addAll(pummokdetail)
        }

        return pummokdetailList
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