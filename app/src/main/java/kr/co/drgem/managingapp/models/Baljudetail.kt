package kr.co.drgem.managingapp.models

data class Baljudetail(
    val seq: String,             //순번
    val pummokcode: String,     //품목코드
    val pummyeong: String,      //품명
    val dobeon_model: String,   //도번/모델
    val saying : String,            //사양
    val balhudanwi : String,        //단위
    val baljusuryang: String,       //발주수량
    val ipgoyejeongil: String,      //입고예정일? 디자인에없음
    val giipgosuryang : String,     //기입고수량
    val jungyojajeyeobu : String,       //중요자재여부
    val location : String,      //위치(공장)


)