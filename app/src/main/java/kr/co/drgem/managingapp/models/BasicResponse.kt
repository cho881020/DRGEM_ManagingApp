package kr.co.drgem.managingapp.models

data class BasicResponse (
    val buseocode: String,
    val buseomyeong: String,
    val changgocode: String,
    val changgomyeong: String,
    val resultcd: String,
    val resultmsg: String,
    val saeopjangcode: String,
    val saeopjangmyeong: String,
    val sawoncode: String,
    val sawonmyeong: String,
    val security_token: String


){



//    fun getUpSeq(): ArrayList<Georaedetail>{
//        val sortList : ArrayList<Georaedetail> = arrayListOf()
//        sortList.clear()
//        sortList.addAll(georaedetail)
//        sortList.sortBy { it.seq }
//        return sortList
//    }
//
//    fun getDownSeq(): ArrayList<Georaedetail>{
//        val sortList : ArrayList<Georaedetail> = arrayListOf()
//        sortList.clear()
//        sortList.addAll(georaedetail)
//        sortList.sortByDescending { it.seq }
//        return sortList
//    }
//
//    fun getUpLocation(): ArrayList<Georaedetail>{
//        val sortList : ArrayList<Georaedetail> = arrayListOf()
//        sortList.clear()
//        sortList.addAll(georaedetail)
//        sortList.sortBy { it.location }
//        return sortList
//    }
//
//    fun getDownLocation(): ArrayList<Georaedetail>{
//        val sortList : ArrayList<Georaedetail> = arrayListOf()
//        sortList.clear()
//        sortList.addAll(georaedetail)
//        sortList.sortByDescending { it.location }
//        return sortList
//    }


}