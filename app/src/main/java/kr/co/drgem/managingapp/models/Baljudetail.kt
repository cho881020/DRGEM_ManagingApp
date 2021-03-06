package kr.co.drgem.managingapp.models

data class Baljudetail(
    val seq: String?,             //순번
    val pummokcode: String?,     //품목코드
    val pummyeong: String?,      //품명
    val dobeon_model: String?,   //도번/모델
    val sayang : String?,            //사양
    val balhudanwi : String?,        //단위
    val baljusuryang: String?,       //발주수량
    val ipgoyejeongil: String?,      //입고예정일? 디자인에없음
    val giipgosuryang : String?,     //기입고수량
    var ipgosuryang : String?,     //입고수량 => 서버에 던져줄 것 (+ 로컬디비에 저장할것)
    val jungyojajeyeobu : String?,       //중요자재여부
    val location : String?,      //위치(공장)


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

    fun getsayangHP() : String {
        if(sayang == null){
            return "-"
        }
        return sayang
    }

    fun getBalhudanwiHP() : String {
        if(balhudanwi == null){
            return "-"
        }
        return balhudanwi
    }

    fun getBaljusuryangHP() : String {
        if(baljusuryang == null){
            return "-"
        }
        return baljusuryang
    }

    fun getIpgoyejeongilHP() : String {
        if(ipgoyejeongil == null){
            return "-"
        }
        return ipgoyejeongil
    }

    fun getGiipgosuryangHP() : String {
        if(giipgosuryang == null){
            return "-"
        }
        return giipgosuryang
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

    private var PummokCount = ""

    fun setPummokCount(count : String) {
        this.PummokCount = count
    }

    fun getPummokCount() : String {

        return PummokCount
    }


    var serialCheck = false

    var itemViewClicked = false


}