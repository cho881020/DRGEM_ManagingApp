package kr.co.drgem.managingapp.localdb

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
                "'${data.georaecheomyeong}', " +
                "'${data.baljubeonho}', " +
                "'${data.georaecheocode}', " +
                "'${data.baljuil}', " +
                "'${data.nappumjangso}', " +
                "'${data.bigo}', " +
                ");"

        db.execSQL(query)

    }

}