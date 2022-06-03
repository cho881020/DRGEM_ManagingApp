package kr.co.drgem.managingapp.models

data class NotDeliveryResponse(
    val pummokcount: String,
    val pummokdetail: ArrayList<PummokdetailDelivery>?,
    val resultcd: String,
    val resultmsg: String
) {


    fun returnPummokdetailDetail(): ArrayList<PummokdetailDelivery> {

        val pummokdetailList = ArrayList<PummokdetailDelivery>()

        if (pummokdetail != null) {
            pummokdetailList.clear()
            pummokdetailList.addAll(pummokdetail)
        }

        return pummokdetailList
    }

}
