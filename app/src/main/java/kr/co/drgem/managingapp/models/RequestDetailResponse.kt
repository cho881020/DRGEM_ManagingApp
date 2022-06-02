package kr.co.drgem.managingapp.models

data class RequestDetailResponse(
    val johoejogeon: Any,
    val migwanri: Any,
    val pummokcount: String,
    val pummokdetail: ArrayList<Pummokdetail>?,
    val requesttype: Any,
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

}