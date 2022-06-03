package kr.co.drgem.managingapp.localdb

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.OrderDetailResponse

class SQLiteDB {
    lateinit var db : SQLiteDatabase

    fun makeDb(localDB: SQLiteDatabase) {
        db = localDB
    }

    fun deleteBaljubeonho() {
        val query = "DELETE FROM baljubeonho;"
        db.execSQL(query)
    }

    fun insertBaljubeonho(data: Baljubeonho) {
        val values = ContentValues()
        values.put("baljuiljastart",data.baljuiljastart)
        values.put("baljuiljaend",data.baljuiljaend)
        values.put("georaecheomyeong",data.georaecheomyeong)
        values.put("baljubeonho",data.baljubeonho)
        values.put("georaecheocode",data.georaecheocode)
        values.put("baljuil",data.baljuil)
        values.put("nappumjangso",data.nappumjangso)
        values.put("bigo",data.bigo)


        db.insert("baljubeonho",null,values)

    }

    fun getAllSavedBaljubeonho() : ArrayList<Baljubeonho> {
        val list = ArrayList<Baljubeonho>()

        val query = "SELECT * FROM baljubeonho;"
        val c = db.rawQuery(query,null)
        while(c.moveToNext()){

            list.add(
                Baljubeonho(
                    "",
                    c.getString(c.getColumnIndex("baljuiljastart")),
                    c.getString(c.getColumnIndex("baljuiljaend")),
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

    fun deleteOrderDetail() {
        val query = "DELETE FROM orderdetail;"
        db.execSQL(query)
    }

    fun insertOrderDetail(data: OrderDetailResponse) {
        val values = ContentValues()
        values.put("georaecheocode",data.georaecheocode)
        values.put("bigo",data.bigo)
        values.put("resultcd",data.resultcd)
        values.put("requesttype",data.requesttype)
        values.put("baljubeonho",data.baljubeonho)
        values.put("nappumcheo",data.nappumcheo)
        values.put("georaecheomyeong",data.georaecheomyeong)
        values.put("baljuil",data.baljuil)
        values.put("nappumjangso",data.nappumjangso)
        values.put("resultmsg",data.resultmsg)


        db.insert("orderdetail",null,values)
    }

    fun getSavedOrderDetail() : ArrayList<OrderDetailResponse> {
        val list = ArrayList<OrderDetailResponse>()

        val query = "SELECT * FROM orderdetail;"
        val c = db.rawQuery(query,null)
        while(c.moveToNext()){

            list.add(
                OrderDetailResponse(
                    c.getString(c.getColumnIndex("georaecheocode")),
                    c.getString(c.getColumnIndex("bigo")),
                    c.getString(c.getColumnIndex("resultcd")),
                    c.getString(c.getColumnIndex("requesttype")),
                    c.getString(c.getColumnIndex("baljubeonho")),
                    c.getString(c.getColumnIndex("nappumcheo")),
                    c.getString(c.getColumnIndex("georaecheomyeong")),
                    c.getString(c.getColumnIndex("baljuil")),
                    null,
                    c.getString(c.getColumnIndex("nappumjangso")),
                    c.getString(c.getColumnIndex("resultmsg"))
                )
            )

        }

        return list
    }

}