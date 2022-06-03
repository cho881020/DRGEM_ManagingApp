package kr.co.drgem.managingapp.localdb

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kr.co.drgem.managingapp.models.Baljubeonho

class SQLiteDB {
    lateinit var db : SQLiteDatabase

    fun makeDb(localDB: SQLiteDatabase) {
        db = localDB
    }

    fun insertBaljubeonho(data: Baljubeonho) {

        val query = "INSERT INTO baljubeonho ('baljuiljastart', " +
                "'baljuiljaend', " +
                "'georaecheomyeong', " +
                "'baljubeonho', " +
                "'georaecheocode', " +
                "'baljuil', " +
                "'nappumjangso', " +
                "'bigo') " +
                "values (" +
                "'${data.baljuiljastart}', " +
                "'${data.baljuiljaend}', " +
                "'${data.georaecheomyeong}', " +
                "'${data.baljubeonho}', " +
                "'${data.georaecheocode}', " +
                "'${data.baljuil}', " +
                "'${data.nappumjangso}', " +
                "'${data.bigo}' " +
                ");"

        db.execSQL(query)

    }

    fun getAllSavedBaljubeonho() : ArrayList<Baljubeonho> {
        val list = ArrayList<Baljubeonho>()

        val query = "SELECT * FROM baljubeonho;"
        val c = db.rawQuery(query,null)
        while(c.moveToNext()){
            System.out.println("txt : "+c.getString(c.getColumnIndex("baljubeonho")));
        }

        return list
    }

}