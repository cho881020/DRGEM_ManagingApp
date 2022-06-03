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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists baljubeonho"
        db.execSQL(sql)
        onCreate(db)
    }

}