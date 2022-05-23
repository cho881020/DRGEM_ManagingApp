package kr.co.drgem.managingapp.models

data class BasicResponse (
    val resultcd: String,
    val resultmsg: String,
    val georaemyeongsebeonho : String,
    val georaeil : String,
    val georaecheocode :String,
    val georaecheomyeong : String,
    val nappumcheo : String,
    val nappumcheomyeong : String,
    val bigo : String,
    val pummokcount : String,
    val georaedetail : ArrayList<Georaedetail>


){



    fun getUpSeq(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        sortList.addAll(georaedetail)
        sortList.sortBy { it.seq }
        return sortList
    }

    fun getDownSeq(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        sortList.addAll(georaedetail)
        sortList.sortByDescending { it.seq }
        return sortList
    }

    fun getUpLocation(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        sortList.addAll(georaedetail)
        sortList.sortBy { it.location }
        return sortList
    }

    fun getDownLocation(): ArrayList<Georaedetail>{
        val sortList : ArrayList<Georaedetail> = arrayListOf()
        sortList.clear()
        sortList.addAll(georaedetail)
        sortList.sortByDescending { it.location }
        return sortList
    }


}