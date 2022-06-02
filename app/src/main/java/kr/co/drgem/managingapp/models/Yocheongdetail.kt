package kr.co.drgem.managingapp.models

data class Yocheongdetail(

    val yocheongil: String?,
    val yocheongbeonho: String?,
    val yocheongchanggo: String?,
    val yocheongja: String?,
    val bigo: String?,
    val chulgojisicode: String?,
    val gongheong: String?,
){

    fun getyocheongilHP() : String {
        if(yocheongil == null){
            return "-"
        }
        return yocheongil
    }

    fun getyocheongbeonhoHP() : String {
        if(yocheongbeonho == null){
            return "-"
        }
        return yocheongbeonho
    }

    fun getyocheongchanggoHP() : String {
        if(yocheongchanggo == null){
            return "-"
        }
        return yocheongchanggo
    }

    fun getyocheongjaHP() : String {
        if(yocheongja == null){
            return "-"
        }
        return yocheongja
    }

    fun getbigoHP() : String {
        if(bigo == null){
            return "-"
        }
        return bigo
    }

    fun getchulgojisicodeHP() : String {
        if(chulgojisicode == null){
            return "-"
        }
        return chulgojisicode
    }

    fun getgongheongHP() : String {
        if(gongheong == null){
            return "-"
        }
        return gongheong
    }

}