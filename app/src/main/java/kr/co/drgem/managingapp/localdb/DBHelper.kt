package kr.co.drgem.managingapp.localdb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db: SQLiteDatabase) {
        // 로그인 공통 정보 테이블
        val sql1 = "CREATE TABLE if not exists LOGIN_WORK_COMMON(" +
                "id integer primary key autoincrement," +
                "USERID text," +
                "USERNAME text," +
                "USERPW text," +
                "SAEOPJANGCODE text," +
                "SAEOPJANGMYEONG text," +
                "BUSEOCODE text," +
                "BUSEOMYEONG text," +
                "CHANGGOCODE text," +
                "CHANGGOMYEONG text," +
                "SECURITY_TOKEN text," +
                "PROGRAM_VERSION text," +
                "INPUTDATE text," +
                "WORKGUBUN text," +
                "WORKNUMBER text," +
                "TABLETIPNUMBER text," +
                "WORK_DATE text," +
                "WORK_STATE text" +
                ");"
        db.execSQL(sql1)

        // 시리얼번호 저장 테이블
        val sql2 = "CREATE TABLE if not exists SERIAL(" +
                "id integer primary key autoincrement," +
                "pummokcode text," +
                "serial text," +
                "position text" +
                ");"
        db.execSQL(sql2)

        // 발주입고 발주번호 테이블
        val sql3 = "CREATE TABLE if not exists baljubeonho(" +
                "id integer primary key autoincrement," +
                //"baljuiljastart text," +
                //"baljuiljaend text," +
                "georaecheomyeong text," +
                "baljubeonho text," +
                "georaecheocode text," +
                "baljuil text," +
                "nappumjangso text," +
                "bigo text" +
                ");"
        db.execSQL(sql3)

//        // 발주입고 선택 발주번호 정보 테이블 - 공통테이블과 합칠 것
//        val sql4 = "CREATE TABLE if not exists orderdetail(" +
//                "id integer primary key autoincrement," +
//                "georaecheocode text," +
//                "bigo text," +
//                "resultcd text," +
//                "requesttype text," +
//                "baljubeonho text," +
//                "nappumcheo text," +
//                "georaecheomyeong text," +
//                "baljuil text," +
//                "nappumjangso text," +
//                "resultmsg text" +
//                ");"
//        db.execSQL(sql4)

        // 발주입고 디테일 정보 테이블
        val sql4 = "CREATE TABLE if not exists baljudetail(" +
                "id integer primary key autoincrement," +
                "seq text," +
                "pummokcode text," +
                "pummyeong text," +
                "dobeon_model text," +
                "sayang text," +
                "balhudanwi text," +
                "baljusuryang text," +
                "ipgoyejeongil text," +
                "giipgosuryang text," +
                "ipgosuryang text," +
                "jungyojajeyeobu text," +
                "location text" +
                ");"
        db.execSQL(sql4)

        // 발주입고 공통 정보 테이블
        val sql5 = "CREATE TABLE if not exists BALJU_INFO_COMMON(" +
                "id integer primary key autoincrement," +
                "BALJUILJASTART     text," +  // 발주시작일자
                "BALJUILJAEND       text," +  // 발주종료일자
                "GEORAECHEOMEONG    text," +  // 거래처명
                "BALJUBEONHO        text," +  // 발주번호
                "BALJUNUMBERCOUNT   text," +  // 발주번호 갯수
                "DETAILCOUNT        text," +  // 상세정보 개수
                "BALJUBEONHOSEL     text," +  // 선택된 발주번호
                "BALJUILSEL         text," +  // 선택된 발주일
                "GEORAECHEOCODESEL  text," +  // 선택된 거래처코드
                "GEORAECHEMYEONGSEL text," +  // 선택된 거래처명
                "BIGOSEL            text," +  // 선택된 비고
                "NAPPUMJANGSOSEL    text," +  // 선택된 납품장소
                "IPGODATE           text," +  // 입고일자
                "IPGOSAUPJANGCODE   text," +  // 입고사업장코드
                "IPGOCHANGGOCODE    text," +  // 입고창고코드
                "IPGODAMDANGJA      text"  +  // 입고담당자
                ");"
        db.execSQL(sql5)

//        val sql7 = "CREATE TABLE if not exists BALJU_DETAIL_INFO(" +
//                "id integer primary key autoincrement," +
//                "IPGODATE text," +
//                "IPGOSAUPJANGCODE text," +
//                "IPGOCHANGGOCODE text," +
//                "IPGODAMDANGJA text" +
//                ");"
//
//        db.execSQL(sql7)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists baljubeonho"
        db.execSQL(sql)
        onCreate(db)
    }
}