package kr.co.drgem.managingapp.models

data class OrderResponse(
    val baljubeonho: ArrayList<Baljubeonho>?,
    var resultcd: String?,
    val resultmsg: String?

){

    fun returnBaljubeonho() : ArrayList<Baljubeonho> {

        val baljuList = ArrayList<Baljubeonho>()

        if(baljubeonho != null){
            baljuList.clear()
            baljuList.addAll(baljubeonho)
        }

        return baljuList
    }



}