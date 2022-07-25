package kr.co.drgem.managingapp.localdb.model
// 발주업무 공통정보 테이블 항목
data class BaljuInfoCommonDB(
    val BALJUILJASTART     : String?,  // 발주시작일자
    val BALJUILJAEND       : String?,  // 발주종료일자
    val GEORAECHEOMEONG    : String?,  // 거래처명
    val BALJUBEONHO        : String?,  // 발주번호
    val BALJUNUMBERCOUNT   : String?,  // 발주번호 갯수
    val DETAILCOUNT        : String?,  // 상세정보 개수
    val BALJUBEONHOSEL     : String?,  // 선택된 발주번호
    val BALJUILSEL         : String?,  // 선택된 발주일
    val GEORAECHEOCODESEL  : String?,  // 선택된 거래처코드
    val GEORAECHEMYEONGSEL : String?,  // 선택된 거래처명
    val BIGOSEL            : String?,  // 선택된 비고
    val NAPPUMJANGSOSEL    : String?,  // 선택된 납품장소
    val IPGODATE           : String?,  // 입고일자
    val IPGOSAUPJANGCODE   : String?,  // 입고사업장코드
    val IPGOCHANGGOCODE    : String?,  // 입고창고코드
    val IPGODAMDANGJA      : String?,  // 입고담당자
) {
}