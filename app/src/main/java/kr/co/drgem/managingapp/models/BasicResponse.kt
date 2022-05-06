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


)