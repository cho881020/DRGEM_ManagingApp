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

    fun getUpYocheongBeonho(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.yocheongbeonho }
        return sortList
    }

    fun getDownYocheongBeonho(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.yocheongbeonho }
        return sortList
    }

    fun getUpPummokcode(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.pummokcode }
        return sortList
    }

    fun getDownPummokcode(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.pummokcode }
        return sortList
    }

    fun getUpPummyeong(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.pummyeong }
        return sortList
    }

    fun getDownPummyeong(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.pummyeong }
        return sortList
    }

    fun getUpDobeonModel(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.dobeon_model }
        return sortList
    }

    fun getDownDobeonModel(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.dobeon_model }
        return sortList
    }

    fun getUpSayang(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.sayang }
        return sortList
    }

    fun getDownSayang(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.sayang }
        return sortList
    }

    fun getUpLocation(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortBy { it.location }
        return sortList
    }

    fun getDownLocation(): ArrayList<PummokdetailDelivery>{
        val sortList : ArrayList<PummokdetailDelivery> = arrayListOf()
        sortList.clear()
        if (pummokdetail != null) {
            sortList.addAll(pummokdetail)
        }
        sortList.sortByDescending { it.location }
        return sortList
    }

}
