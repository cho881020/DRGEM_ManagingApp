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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists baljubeonho"
        db.execSQL(sql)
        onCreate(db)
    }

}