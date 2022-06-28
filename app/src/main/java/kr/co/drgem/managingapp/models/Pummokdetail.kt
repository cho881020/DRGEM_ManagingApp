package kr.co.drgem.managingapp.models

data class Pummokdetail(
    val pummokcode: String?,     // 품목코드
    val pummyeong: String?,      // 품명
    val dobeon_model: String?,   //도번/모델
    val sayang: String?,         //사양
    val danwi: String?,          //단위
    val location: String?,       //위치
    var hyeonjaegosuryang: String?,      //현재고수량
    val yocheongsuryang: String?,        //요청수량
    val gichulgosuryang: String?,        //기출고수량
    val chulgosuryang: String?,          //출고수량
    val jungyojajeyeobu: String?,        //중요자재여부
    val yocheongbeonho: String?,        //요청번호



) {

    fun getPummokcodeHP() : String {
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
            return "0"
        }
        return hyeonjaegosuryang as String
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
        if(chulgosuryang.isNullOrEmpty()){
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

    fun getyocheongbeonhoHP() : String {
        if(yocheongbeonho == null){
            return "-"
        }
        return yocheongbeonho
    }



    private var josasigan = ""

    fun setJosasiganAdd( josasigan : String) {
        this.josasigan = josasigan
    }

    fun getJosasiganAdd() : String {

        return josasigan
    }


    private var locationAdd = ""

    fun setLocationAdd( locationAdd : String) {
        this.locationAdd = locationAdd
    }

    fun getLocationAdd() : String {

        return locationAdd
    }


// 시리얼카운트 : 입력카운트
    private var PummokCount = ""

    fun setPummokCount(count : String) {
        this.PummokCount = count
    }

    fun getPummokCount() : String {

        return PummokCount
    }

    var serialCheck = false


}