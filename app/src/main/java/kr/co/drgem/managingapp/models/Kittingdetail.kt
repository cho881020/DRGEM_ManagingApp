package kr.co.drgem.managingapp.models

data class Kittingdetail(
    val kittingbeonho: String?,         // 키팅번호
    val kittingdeungrokil: String?,     //키팅등록일
    val kittingja: String?,             //키팅자
    val bulchulchanggo: String?,        //불출창고
    val bigo: String?,                  //비고
    val yocheongbeonho: String?,        //요청번호
    val chulgojisicode: String?,        //출하요청서번호
    val deviceinfo: String?,            //디바이스정보
) {


    fun getKittingbeonhoHP() : String {
        if(kittingbeonho == null){
            return "-"
        }
        return kittingbeonho
    }

    fun getKittingdeungrokilHP() : String {
        if(kittingdeungrokil == null){
            return "-"
        }
        return kittingdeungrokil
    }

    fun getKittingjaHP() : String {
        if(kittingja == null){
            return "-"
        }
        return kittingja
    }

    fun getBulchulchanggoHP() : String {
        if(bulchulchanggo == null){
            return "-"
        }
        return bulchulchanggo
    }

    fun getBigoHP() : String {
        if(bigo == null){
            return "-"
        }
        return bigo
    }

    fun getYocheongbeonhoHP() : String {
        if(yocheongbeonho == null){
            return "-"
        }
        return yocheongbeonho
    }

    fun getChulgojisicodeHP() : String {
        if(chulgojisicode == null){
            return "-"
        }
        return chulgojisicode
    }

    fun getDeviceinfoHP() : String {
        if(deviceinfo == null){
            return "-"
        }
        return deviceinfo
    }

}