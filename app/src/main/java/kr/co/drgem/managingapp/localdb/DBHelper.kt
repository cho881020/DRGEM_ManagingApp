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
        val sql = "CREATE TABLE if not exists baljubeonho(" +
                "id integer primary key autoincrement," +
                "baljuiljastart text," +
                "baljuiljaend text," +
                "georaecheomyeong text," +
                "baljubeonho text," +
                "georaecheocode text," +
                "baljuil text," +
                "nappumjangso text," +
                "bigo text" +
                ");"

        db.execSQL(sql)

        val sql2 = "CREATE TABLE if not exists orderdetail(" +
                "id integer primary key autoincrement," +
                "georaecheocode text," +
                "bigo text," +
                "resultcd text," +
                "requesttype text," +
                "baljubeonho text," +
                "nappumcheo text," +
                "georaecheomyeong text," +
                "baljuil text," +
                "nappumjangso text," +
                "resultmsg text" +
                ");"

        db.execSQL(sql2)


        val sql3 = "CREATE TABLE if not exists baljudetail(" +
                "id integer primary key autoincrement," +
                "seq text," +
                "pummokcode text," +
                "pummyeong text," +
                "dobeon_model text," +
                "saying text," +
                "balhudanwi text," +
                "baljusuryang text," +
                "ipgoyejeongil text," +
                "giipgosuryang text," +
                "ipgosuryang text," +
                "jungyojajeyeobu text," +
                "location text" +
                ");"

        db.execSQL(sql3)


        val sql4 = "CREATE TABLE if not exists LOGIN_WORK_COMMON(" +
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

        db.execSQL(sql4)



        val sql5 = "CREATE TABLE if not exists SERIAL(" +
                "id integer primary key autoincrement," +
                "pummokcode text," +
                "serial text," +
                "position text" +
                ");"

        db.execSQL(sql5)



        val sql6 = "CREATE TABLE if not exists BALJU_COMMON(" +
                "id integer primary key autoincrement," +
                "BALJUILJASTART text," +
                "BALJUILJAEND text," +
                "GEORAECHEOMEONG text," +
                "BALJUBEONHO text," +
                "BALJUCOUNT text," +
                "BALJUBEONHOSEL text," +
                "BALJUIL text," +
                "GEORAECHEOCODE text," +
                "GEORAECHEMYEONG text," +
                "BIGO text," +
                "IPGODATE text," +
                "IPGOSAUPJANGCODE text," +
                "IPGOCHANGGOCODE text," +
                "IPGODAMDANGJA text" +
                ");"

        db.execSQL(sql6)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists baljubeonho"
        db.execSQL(sql)
        onCreate(db)
    }

}