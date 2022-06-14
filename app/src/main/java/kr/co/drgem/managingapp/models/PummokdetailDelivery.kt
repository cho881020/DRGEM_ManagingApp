package kr.co.drgem.managingapp.models

data class PummokdetailDelivery(
    val yocheongil: String?,
    val yocheongbeonho: String?,
    val yocheongchanggo: String?,
    val yocheongja: String?,
    val pummokcode: String?,
    val pummyeong: String?,
    val dobeon_model: String?,
    val sayang: String?,
    val danwi: String?,
    val location: String?,
    val hyeonjaegosuryang: String?,
    val yocheongsuryang: String?,
    val gichulgosuryang: String?,
    val chulgosuryang: String?,
    val jungyojajeyeobu: String?,

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

    fun getpummokcodeHP() : String {
        if(pummokcode == null){
            return "-"
        }
        return pummokcode
    }

    fun getpummyeongHP() : String {
        if(pummyeong == null){
            return "-"
        }
        return pummyeong
    }

    fun getdobeon_modelHP() : String {
        if(dobeon_model == null){
            return "-"
        }
        return dobeon_model
    }

    fun getsayangHP() : String {
        if(sayang == null){
            return "-"
        }
        return sayang
    }

    fun getdanwiHP() : String {
        if(danwi == null){
            return "-"
        }
        return danwi
    }

    fun getlocationHP() : String {
        if(location == null){
            return "-"
        }
        return location
    }

    fun gethyeonjaegosuryangHP() : String {
        if(hyeonjaegosuryang == null){
            return "-"
        }
        return hyeonjaegosuryang
    }

    fun getyocheongsuryangHP() : String {
        if(yocheongsuryang == null){
            return "-"
        }
        return yocheongsuryang
    }

    fun getgichulgosuryangHP() : String {
        if(gichulgosuryang == null){
            return "-"
        }
        return gichulgosuryang
    }

    fun getchulgosuryangHP() : String {
        if(chulgosuryang == null){
            return "0"
        }
        return chulgosuryang
    }

    fun getjungyojajeyeobuHP() : String {
        if(jungyojajeyeobu == null){
            return "-"
        }
        return jungyojajeyeobu
    }

    private var SerialCount = ""

    fun setSerialCount( count : String) {
        this.SerialCount = count
    }

    fun getSerialCount() : String {

        return SerialCount
    }


}