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


}