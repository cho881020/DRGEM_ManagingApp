package kr.co.drgem.managingapp.models

data class RequestResponse(
    val resultcd: String,
    val resultmsg: String,
    val yocheongcount: String,
    val yocheongdetail: List<Yocheongdetail>?
){

    fun returnYocheongDetail() : ArrayList<Yocheongdetail> {

        val yocheongdetailList = ArrayList<Yocheongdetail>()

        if(yocheongdetail != null){
            yocheongdetailList.clear()
            yocheongdetailList.addAll(yocheongdetail)
        }

        return yocheongdetailList
    }

}