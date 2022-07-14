package kr.co.drgem.managingapp.models

data class PummokdetailStock(
    var seqnum: String?,              // 시퀀스 번호
    var pummokcode: String?,          // 품목코드
    var pummyeong: String?,           // 품명
    var dobeon_model: String?,        // 도번/모델
    var sayang: String?,              // 사양
    var danwi: String?,               // 단위
    var location: String?,            // 위치
    var locationadd: String?,         // 로케이션
    var hyeonjaegosuryang: String?,   // 현재고수량
    var josasigan: String?,           // 조사시간
) {
    fun getSeqNumHP() : String {
        if(seqnum == null){
            return "-"
        }
        return seqnum as String
    }

    fun getPummokcodeHP() : String {
        if(pummokcode == null){
            return "-"
        }
        return pummokcode as String
    }

    fun getpummyeongHP() : String {
        if(pummyeong == null){
            return "-"
        }
        return pummyeong as String
    }

    fun getdobeon_modelHP() : String {
        if(dobeon_model == null){
            return "-"
        }
        return dobeon_model as String
    }

    fun getsayangHP() : String {
        if(sayang == null){
            return "-"
        }
        return sayang as String
    }

    fun getdanwiHP() : String {
        if(danwi == null){
            return "-"
        }
        return danwi as String
    }

    fun getlocationHP() : String {
        if(location == null){
            return "-"
        }
        return location as String
    }

    fun getLocationaddHP() : String {
        if(locationadd == null){
            return "-"
        }
        return locationadd as String
    }

    fun gethyeonjaegosuryangHP() : String {
        if(hyeonjaegosuryang == null){
            return "0"
        }
        return hyeonjaegosuryang as String
    }

    fun getjosasiganHP() : String {
        if(josasigan == null){
            return "-"
        }
        return josasigan as String
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

    var itemViewClicked = false

}