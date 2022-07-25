package kr.co.drgem.managingapp.localdb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import kr.co.drgem.managingapp.localdb.model.BaljuInfoCommonDB
import kr.co.drgem.managingapp.localdb.model.BaljuCommonLocalDB  // 폐기대상
import kr.co.drgem.managingapp.localdb.model.BaljuDetailInfoLocalDB // 폐기대상
import kr.co.drgem.managingapp.localdb.model.LoginWorkCommonLocalDB
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.OrderDetailResponse
import kr.co.drgem.managingapp.utils.IPUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SQLiteDB {
    lateinit var db: SQLiteDatabase

    fun makeDb(localDB: SQLiteDatabase) {
        db = localDB
    }

    // 개인 로그인 정보와 작업 상황 정보를 로그인 공통 테이블에 저장
    fun insertLoginWorkCommon(
        userId: String,
        userPw: String,
        data  : BasicResponse
    ) {
        val values = ContentValues()
        values.put("USERID"         , userId)
        values.put("USERNAME"       , data.sawonmyeong)
        values.put("USERPW"         , userPw)
        values.put("SAEOPJANGCODE"  , data.saeopjangcode)
        values.put("SAEOPJANGMYEONG", data.saeopjangmyeong)
        values.put("BUSEOCODE"      , data.buseocode)
        values.put("BUSEOMYEONG"    , data.buseomyeong)
        values.put("CHANGGOCODE"    , data.changgocode)
        values.put("CHANGGOMYEONG"  , data.changgomyeong)
        values.put("SECURITY_TOKEN" , data.security_token)
        values.put("PROGRAM_VERSION", data.program_version)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        values.put("INPUTDATE"      , simpleDateFormat.format(Calendar.getInstance().time))
        values.put("WORKGUBUN"      , "None") //01:거래,02:발주,03:키팅,04:요청,05:미출자재,06:로케이션,07:재고 선택되면 update
        values.put("WORKNUMBER"     , "None") // worknumber요청하여 받은 순간 update
        values.put("TABLETIPNUMBER" , IPUtil.getIpAddress())
        values.put("WORK_DATE"      , "TEMP") // 첫 333 전송 시간? update
        values.put("WORK_STATE"     , "000") //초기값 '000', 작업시작(관리번호받으면)'111', 개별전송 '333', 전송완료'777' update

        db.insert("LOGIN_WORK_COMMON", null, values)  // 로그인이 완료된 순간
    }

    @SuppressLint("Range")
    fun getAllLoginWorkCommon(): ArrayList<LoginWorkCommonLocalDB> {
        val list = ArrayList<LoginWorkCommonLocalDB>()

        val query = "SELECT * FROM LOGIN_WORK_COMMON;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            list.add(
                LoginWorkCommonLocalDB(
                    c.getString(c.getColumnIndex("USERID")),
                    c.getString(c.getColumnIndex("USERNAME")),
                    c.getString(c.getColumnIndex("USERPW")),
                    c.getString(c.getColumnIndex("SAEOPJANGCODE")),
                    c.getString(c.getColumnIndex("SAEOPJANGMYEONG")),
                    c.getString(c.getColumnIndex("BUSEOCODE")),
                    c.getString(c.getColumnIndex("BUSEOMYEONG")),
                    c.getString(c.getColumnIndex("CHANGGOCODE")),
                    c.getString(c.getColumnIndex("CHANGGOMYEONG")),
                    c.getString(c.getColumnIndex("SECURITY_TOKEN")),
                    c.getString(c.getColumnIndex("PROGRAM_VERSION")),
                    c.getString(c.getColumnIndex("INPUTDATE")),
                    c.getString(c.getColumnIndex("WORKGUBUN")),
                    c.getString(c.getColumnIndex("WORKNUMBER")),
                    c.getString(c.getColumnIndex("TABLETIPNUMBER")),
                    c.getString(c.getColumnIndex("WORK_DATE")),
                    c.getString(c.getColumnIndex("WORK_STATE"))
                )
            )
        }
        return list
    }

    fun updateLoginWorkCommonWorkGubun(WorkGubun: String) {  // 1개의 레코드로 운용되기 때문에 무조건 update
        val query = "UPDATE LOGIN_WORK_COMMON SET WORKGUBUN='${WorkGubun}' "

        db.execSQL(query)
    }

    fun updateLoginWorkCommonWorkState(WorkState: String) {  // 1개의 레코드로 운용되기 때문에 무조건 update
        val query = "UPDATE LOGIN_WORK_COMMON SET WORKGUBUN='${WorkState}' "

        db.execSQL(query)
    }

    fun deleteLoginWorkCommon() {
        val query = "DELETE FROM LOGIN_WORK_COMMON;"
        db.execSQL(query)
    }

    fun updateWorkInfo(workType: String, workSEQ: String, status: String) {
        val query =
            "UPDATE LOGIN_WORK_COMMON SET WORKGUBUN='${workType}', WORKNUMBER = '${workSEQ}', WORK_STATE = '${status}'"
        db.execSQL(query)
    }

    // 시리얼번호 테이블-----------------------------------------------------

    fun deleteAllSerials() {
        val query = "DELETE FROM SERIAL;"
        db.execSQL(query)
    }

    fun deletePummokcodeSerials(pummokcode: String) {
        val query = "DELETE FROM SERIAL WHERE pummokcode='${pummokcode}';"
        db.execSQL(query)
    }

    fun insertSerialToPummokcode(
        pummokcode: String,
        serial    : String,
        position  : String,
    ) {
        val values = ContentValues()
        values.put("pummokcode", pummokcode)
        values.put("serial"    , serial)
        values.put("position"  , position)

        db.insert("SERIAL", null, values)
    }

    @SuppressLint("Range")
    fun getAllSerialByPummokcode(pummokcode: String): ArrayList<SerialLocalDB> {
        val list = ArrayList<SerialLocalDB>()

        val query = "SELECT * FROM SERIAL WHERE pummokcode='${pummokcode}';"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            list.add(
                SerialLocalDB(
                    c.getString(c.getColumnIndex("pummokcode")),
                    c.getString(c.getColumnIndex("serial")),
                    c.getString(c.getColumnIndex("position")),
                )
            )
        }
        return list
    }

    @SuppressLint("Range")
    fun getFirstSerialByPummokcodeAndPosition(pummokcode:String, position: String) : SerialLocalDB? {

        val query =
            "SELECT * FROM SERIAL WHERE pummokcode='${pummokcode}' AND  position='${position}';"
        val c = db.rawQuery(query, null)

        if (c.moveToNext()) {
            return SerialLocalDB(
                c.getString(c.getColumnIndex("pummokcode")),
                c.getString(c.getColumnIndex("serial")),
                c.getString(c.getColumnIndex("position")),
            )
        } else {
            return null
        }
    }

    // 발주입고(매입입고) 테이블------------------------------------------------
    fun deleteBaljubeonho() {
        val query = "DELETE FROM baljubeonho;"
        db.execSQL(query)
    }

    fun insertBaljubeonho(data: Baljubeonho) {
        val values = ContentValues()
//        values.put("baljuiljastart"  , data.baljuiljastart)
//        values.put("baljuiljaend"    , data.baljuiljaend)
        values.put("georaecheomyeong", data.georaecheomyeong)
        values.put("baljubeonho"     , data.baljubeonho)
        values.put("georaecheocode"  , data.georaecheocode)
        values.put("baljuil"         , data.baljuil)
        values.put("nappumjangso"    , data.nappumjangso)
        values.put("bigo"            , data.bigo)

        db.insert("baljubeonho", null, values)
    }

    @SuppressLint("Range")
    fun getAllSavedBaljubeonho(): ArrayList<Baljubeonho> {
        val list = ArrayList<Baljubeonho>()

        val query = "SELECT * FROM baljubeonho;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {

            list.add(
                Baljubeonho(
//                    "",
//                    c.getString(c.getColumnIndex("baljuiljastart")),
//                    c.getString(c.getColumnIndex("baljuiljaend")),
                    c.getString(c.getColumnIndex("georaecheomyeong")),
                    c.getString(c.getColumnIndex("baljubeonho")),
                    c.getString(c.getColumnIndex("georaecheocode")),
                    c.getString(c.getColumnIndex("baljuil")),
                    c.getString(c.getColumnIndex("nappumjangso")),
                    c.getString(c.getColumnIndex("bigo"))
                )
            )
        }
        return list
    }

    // 삭제
//    fun deleteOrderDetail() {
//        //val query = "DELETE FROM orderdetail;"  // 발주 공통과 합친다.
//        //db.execSQL(query)
//
//        val query2 = "DELETE FROM baljudetail;"
//        db.execSQL(query2)
//    }

    fun insertOrderDetail(data: OrderDetailResponse) {
        // 아래의 문장은 삭제한다. 공통정보에 축소 통합
//        val values = ContentValues()
//        values.put("georaecheocode"  , data.georaecheocode)
//        values.put("bigo"            , data.bigo)
//        values.put("resultcd"        , data.resultcd)
//        values.put("requesttype"     , data.requesttype)
//        values.put("baljubeonho"     , data.baljubeonho)
//        values.put("nappumcheo"      , data.nappumcheo)
//        values.put("georaecheomyeong", data.georaecheomyeong)
//        values.put("baljuil"         , data.baljuil)
//        values.put("nappumjangso"    , data.nappumjangso)
//        values.put("resultmsg"       , data.resultmsg)
//
//        db.insert("orderdetail", null, values)
//
//        deleteBaljuDetail()

        for (balju_detail in data.returnBaljudetail()) {
            insertBaljuDetail(balju_detail)

            Log.d("로컬DB에들어가는발주상세", balju_detail.pummyeong.toString())
        }
    }

    // 원래는 orderdetail에서 읽어 왔으나 공통테이블로 바뀌었다.
    // 복구시 불필요한 데이터는 저장하지 않았다.
    @SuppressLint("Range")
    fun getSavedOrderDetail(): ArrayList<OrderDetailResponse> {
        val list = ArrayList<OrderDetailResponse>()

//        val query = "SELECT * FROM orderdetail;"
//        val c = db.rawQuery(query, null)
//        while (c.moveToNext()) {

//            val orderDetail = OrderDetailResponse(
//                c.getString(c.getColumnIndex("georaecheocode")),
//                c.getString(c.getColumnIndex("bigo")),
//                c.getString(c.getColumnIndex("resultcd")),
//                c.getString(c.getColumnIndex("requesttype")),
//                c.getString(c.getColumnIndex("baljubeonho")),
//                c.getString(c.getColumnIndex("nappumcheo")),
//                c.getString(c.getColumnIndex("georaecheomyeong")),
//                c.getString(c.getColumnIndex("baljuil")),
//                ArrayList(),
//                c.getString(c.getColumnIndex("nappumjangso")),
//                c.getString(c.getColumnIndex("resultmsg"))
//            )

        val query = "SELECT * FROM BALJU_INFO_COMMON;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            val orderDetail = OrderDetailResponse(
                c.getString(c.getColumnIndex("GEORAECHEOCODESEL")),
                c.getString(c.getColumnIndex("BIGOSEL")),
                "",
                "",
                c.getString(c.getColumnIndex("BALJUBEONHOSEL")),
                "",
                c.getString(c.getColumnIndex("GEORAECHEMYEONGSEL")),
                c.getString(c.getColumnIndex("BALJUILSEL")),
                ArrayList(),
                c.getString(c.getColumnIndex("NAPPUMJANGSOSEL")),
                ""
            )
            orderDetail.baljudetail?.addAll(getAllSavedBaljuDetail())

            Log.d("주문상세의발주상세갯수", orderDetail.returnBaljudetail().size.toString())
            list.add(
                orderDetail
            )
        }

        return list
    }

    fun deleteBaljuDetail() {
        val query = "DELETE FROM baljudetail;"
        db.execSQL(query)
    }

    fun insertBaljuDetail(data: Baljudetail) {
        val values = ContentValues()
        values.put("seq"            , data.seq)
        values.put("pummokcode"     , data.pummokcode)
        values.put("pummyeong"      , data.pummyeong)
        values.put("dobeon_model"   , data.dobeon_model)
        values.put("sayang"         , data.sayang)
        values.put("balhudanwi"     , data.balhudanwi)
        values.put("baljusuryang"   , data.baljusuryang)
        values.put("ipgoyejeongil"  , data.ipgoyejeongil)
        values.put("giipgosuryang"  , data.giipgosuryang)
        values.put("ipgosuryang"    , data.ipgosuryang)
        values.put("jungyojajeyeobu", data.jungyojajeyeobu)
        values.put("location"       , data.location)

        db.insert("baljudetail", null, values)
    }

    @SuppressLint("Range")
    fun getAllSavedBaljuDetail(): ArrayList<Baljudetail> {
        val list = ArrayList<Baljudetail>()

        val query = "SELECT * FROM baljudetail;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {

            Log.d("발주디테일조회", c.getString(c.getColumnIndex("pummyeong")))
            list.add(
                Baljudetail(
                    c.getString(c.getColumnIndex("seq")),
                    c.getString(c.getColumnIndex("pummokcode")),
                    c.getString(c.getColumnIndex("pummyeong")),
                    c.getString(c.getColumnIndex("dobeon_model")),
                    c.getString(c.getColumnIndex("sayang")),
                    c.getString(c.getColumnIndex("balhudanwi")),
                    c.getString(c.getColumnIndex("baljusuryang")),
                    c.getString(c.getColumnIndex("ipgoyejeongil")),
                    c.getString(c.getColumnIndex("giipgosuryang")),
                    c.getString(c.getColumnIndex("ipgosuryang")),
                    c.getString(c.getColumnIndex("jungyojajeyeobu")),
                    c.getString(c.getColumnIndex("location"))
                )
            )
        }
        return list
    }

    fun updateBaljuDetail(data: Baljudetail) {
        val query =
            "UPDATE baljudetail SET ipgosuryang='${data.ipgosuryang}' WHERE pummokcode = '${data.getPummokcodeHP()}'"
        db.execSQL(query)
    }

    fun deleteBaljuInfoCommon() {
        val query = "DELETE FROM BALJU_INFO_COMMON;"
        db.execSQL(query)
    }

    // 발주번호 검색후 list조회 조건 update
    // 발생할 일이 없다. 발주번호 검색시 insert 한번만 실행하면 된다.
    // 발주품목 명세 받은 후 선택된 발주번호의 상세정보와
    //                    서버전달 지정 추가정보 update (입고일자,입고사업장코드,입고창고코드,입고담당자)
    fun updateBaljuInfoCommon(
        BALJUBEONHOSEL     : String?,  // 선택된 발주번호
        BALJUILSEL         : String?,  // 선택된 발주일
        GEORAECHEOCODESEL  : String?,  // 선택된 거래처코드
        GEORAECHEMYEONGSEL : String?,  // 선택된 거래처명
        BIGOSEL            : String?,  // 선택된 비고
        NAPPUMJANGSOSEL    : String?,  // 선택된 납품장소
        IPGODATE           : String?,  // 입고일자
        IPGOSAUPJANGCODE   : String?,  // 입고사업장코드
        IPGOCHANGGOCODE    : String?,  // 입고창고코드
        IPGODAMDANGJA      : String?,  // 입고담당자
    ) {
        val query =
            "UPDATE BALJU_INFO_COMMON SET BALJUBEONHOSEL     ='${BALJUBEONHOSEL}',"     +
                    " BALJUILSEL         ='${BALJUILSEL}',"         +
                    " GEORAECHEOCODESEL  ='${GEORAECHEOCODESEL}',"  +
                    " GEORAECHEMYEONGSEL ='${GEORAECHEMYEONGSEL}'," +
                    " BIGOSEL            ='${BIGOSEL}',"            +
                    " NAPPUMJANGSOSEL    ='${NAPPUMJANGSOSEL}',"    +
                    " IPGODATE           ='${IPGODATE}',"           +
                    " IPGOSAUPJANGCODE   ='${IPGOSAUPJANGCODE}',"   +
                    " IPGOCHANGGOCODE    ='${IPGOCHANGGOCODE}',"    +
                    " IPGODAMDANGJA      ='${IPGODAMDANGJA}'"

        db.execSQL(query)
    }

    // 시리얼번호 입력 또는 수량 입력 후 update
    // 서버전달 지정 추가정보 update (입고일자,입고사업장코드,입고창고코드,입고담당자)
    fun updateBaljuInfoCommon1(
        IPGODATE           : String?,  // 입고일자
        IPGOSAUPJANGCODE   : String?,  // 입고사업장코드
        IPGOCHANGGOCODE    : String?,  // 입고창고코드
        IPGODAMDANGJA      : String?,  // 입고담당자
    ) {
        val query =
            "UPDATE BALJU_INFO_COMMON SET IPGODATE         ='${IPGODATE}',"           +
                                        " IPGOSAUPJANGCODE ='${IPGOSAUPJANGCODE}',"   +
                                        " IPGOCHANGGOCODE  ='${IPGOCHANGGOCODE}',"    +
                                        " IPGODAMDANGJA    ='${IPGODAMDANGJA}'"

        db.execSQL(query)
    }

    fun insertBaljuInfoCommon(
        BALJUILJASTART     : String?,  // 발주시작일자
        BALJUILJAEND       : String?,  // 발주종료일자
        GEORAECHEOMEONG    : String?,  // 거래처명
        BALJUBEONHO        : String?,  // 발주번호
        BALJUNUMBERCOUNT   : String?,  // 발주번호 갯수
        DETAILCOUNT        : String?,  // 상세정보 개수
        BALJUBEONHOSEL     : String?,  // 선택된 발주번호
        BALJUILSEL         : String?,  // 선택된 발주일
        GEORAECHEOCODESEL  : String?,  // 선택된 거래처코드
        GEORAECHEMYEONGSEL : String?,  // 선택된 거래처명
        BIGOSEL            : String?,  // 선택된 비고
        NAPPUMJANGSOSEL    : String?,  // 선택된 납품장소
        IPGODATE           : String?,  // 입고일자
        IPGOSAUPJANGCODE   : String?,  // 입고사업장코드
        IPGOCHANGGOCODE    : String?,  // 입고창고코드
        IPGODAMDANGJA      : String?,  // 입고담당자
    ) {
        val values = ContentValues()
        values.put("BALJUILJASTART"     , BALJUILJASTART)     // 발주시작일자
        values.put("BALJUILJAEND"       , BALJUILJAEND)       // 발주종료일자
        values.put("GEORAECHEOMEONG"    , GEORAECHEOMEONG)    // 거래처명
        values.put("BALJUBEONHO"        , BALJUBEONHO)        // 발주번호
        values.put("BALJUNUMBERCOUNT"   , BALJUNUMBERCOUNT)   // 발주번호 갯수
        values.put("DETAILCOUNT"        , DETAILCOUNT)        // 상세정보 개수
        values.put("BALJUBEONHOSEL"     , BALJUBEONHOSEL)     // 선택된 발주번호
        values.put("BALJUILSEL"         , BALJUILSEL)         // 선택된 발주일
        values.put("GEORAECHEOCODESEL"  , GEORAECHEOCODESEL)  // 선택된 거래처코드
        values.put("GEORAECHEMYEONGSEL" , GEORAECHEMYEONGSEL) // 선택된 거래처명
        values.put("BIGOSEL"            , BIGOSEL)            // 선택된 비고
        values.put("NAPPUMJANGSOSEL"    , NAPPUMJANGSOSEL)    // 선택된 납품장소
        values.put("IPGODATE"           , IPGODATE)           // 입고일자
        values.put("IPGOSAUPJANGCODE"   , IPGOSAUPJANGCODE)   // 입고사업장코드
        values.put("IPGOCHANGGOCODE"    , IPGOCHANGGOCODE)    // 입고창고코드
        values.put("IPGODAMDANGJA"      , IPGODAMDANGJA)      // 입고담당자

        db.insert("BALJU_INFO_COMMON", null, values)
    }

    @SuppressLint("Range")
    fun getAllBaljuInfoCommon(): ArrayList<BaljuInfoCommonDB> {
        val list = ArrayList<BaljuInfoCommonDB>()

        val query = "SELECT * FROM BALJU_INFO_COMMON;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            list.add(
                BaljuInfoCommonDB(
                    c.getString(c.getColumnIndex("BALJUILJASTART")),    // 발주시작일자
                    c.getString(c.getColumnIndex("BALJUILJAEND")),      // 발주종료일자
                    c.getString(c.getColumnIndex("GEORAECHEOMEONG")),   // 거래처명
                    c.getString(c.getColumnIndex("BALJUBEONHO")),       // 발주번호
                    c.getString(c.getColumnIndex("BALJUNUMBERCOUNT")),   // 발주번호 갯수
                    c.getString(c.getColumnIndex("DETAILCOUNT")),        // 상세정보 개수
                    c.getString(c.getColumnIndex("BALJUBEONHOSEL")),     // 선택된 발주번호
                    c.getString(c.getColumnIndex("BALJUILSEL")),      // 선택된 발주일
                    c.getString(c.getColumnIndex("GEORAECHEOCODESEL")),  // 선택된 거래처코드
                    c.getString(c.getColumnIndex("GEORAECHEMYEONGSEL")), // 선택된 거래처명
                    c.getString(c.getColumnIndex("BIGOSEL")),            // 선택된 비고
                    c.getString(c.getColumnIndex("NAPPUMJANGSOSEL")),    // 선택된 납품장소
                    c.getString(c.getColumnIndex("IPGODATE")),          // 입고일자
                    c.getString(c.getColumnIndex("IPGOSAUPJANGCODE")),  // 입고사업장코드
                    c.getString(c.getColumnIndex("IPGOCHANGGOCODE")),   // 입고창고코드
                    c.getString(c.getColumnIndex("IPGODAMDANGJA")),     // 입고담당자
                )
            )
        }
        return list
    }
}