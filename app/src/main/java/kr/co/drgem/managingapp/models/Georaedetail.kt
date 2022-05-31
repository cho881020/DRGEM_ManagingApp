package kr.co.drgem.managingapp.models

data class Georaedetail(
    val seq : String?,
    val pummokcode : String?,
    val pummyeong : String?,
    val dobeon_model : String?,
    val saying : String?,
    val balhudanwi : String?,
    val baljubeonho : String?,
    val baljusuryang : String?,
    val giipgosuryang : String?,
    val ipgosuryang : String?,
    val baljuseq : String?,
    val jungyojajeyeobu : String?,
    val location : String?,

//    val georaesuryang : String,

){

    fun getSeqHP() : String {
        if(seq == null){
            return "-"
        }
        return seq
    }

    fun getPummokcodeHP() : String {
        if(pummokcode == null){
            return "-"
        }
        return pummokcode
    }

    fun getPummyeongHP() : String {
        if(pummyeong == null){
            return "-"
        }
        return pummyeong
    }

    fun getDobeonModelHP() : String {
        if(dobeon_model == null){
            return "-"
        }
        return dobeon_model
    }

    fun getSayingHP() : String {
        if(saying == null){
            return "-"
        }
        return saying
    }

    fun getBalhudanwiHP() : String {
        if(balhudanwi == null){
            return "-"
        }
        return balhudanwi
    }

    fun getBaljubeonhoHP() : String {
        if(baljubeonho == null){
            return "-"
        }
        return baljubeonho
    }

    fun getBaljusuryangHP() : String {
        if(baljusuryang == null){
            return "-"
        }
        return baljusuryang
    }

    fun getGiipgosuryangHP() : String {
        if(giipgosuryang == null){
            return "-"
        }
        return giipgosuryang
    }

    fun getIpgosuryangHP() : String {
        if(ipgosuryang == null){
            return "-"
        }
        return ipgosuryang
    }

    fun getBaljuseqHP() : String {
        if(baljuseq == null){
            return "-"
        }
        return baljuseq
    }


    fun getJungyojajeyeobuHP() : String {
        if(jungyojajeyeobu.isNullOrEmpty()){
            return "-"
        }
        return jungyojajeyeobu
    }

    fun getLocationHP() : String {
        if(location == null){
            return "-"
        }
        return location
    }



}
