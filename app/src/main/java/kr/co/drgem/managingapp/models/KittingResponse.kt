package kr.co.drgem.managingapp.models

data class KittingResponse(
    val kittingcount: String,
    val kittingdetail: ArrayList<Kittingdetail>?,
    val resultcd: String,
    val resultmsg: String
){

    fun returnKittingDetail() : ArrayList<Kittingdetail> {

        val kittingList = ArrayList<Kittingdetail>()

        if(kittingdetail != null){
            kittingList.clear()
            kittingList.addAll(kittingdetail)
        }

        return kittingList
    }


}