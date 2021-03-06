package kr.co.drgem.managingapp.localdb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import kr.co.drgem.managingapp.localdb.model.BaljuCommonLocalDB
import kr.co.drgem.managingapp.localdb.model.BaljuDetailInfoLocalDB
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

    fun deleteBaljubeonho() {
        val query = "DELETE FROM baljubeonho;"
        db.execSQL(query)
    }

    fun insertBaljubeonho(data: Baljubeonho) {
        val values = ContentValues()
        values.put("baljuiljastart", data.baljuiljastart)
        values.put("baljuiljaend", data.baljuiljaend)
        values.put("georaecheomyeong", data.georaecheomyeong)
        values.put("baljubeonho", data.baljubeonho)
        values.put("georaecheocode", data.georaecheocode)
        values.put("baljuil", data.baljuil)
        values.put("nappumjangso", data.nappumjangso)
        values.put("bigo", data.bigo)


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


        val query2 = "DELETE FROM baljudetail;"
        db.execSQL(query2)
    }

    fun insertOrderDetail(data: OrderDetailResponse) {
        val values = ContentValues()
        values.put("georaecheocode", data.georaecheocode)
        values.put("bigo", data.bigo)
        values.put("resultcd", data.resultcd)
        values.put("requesttype", data.requesttype)
        values.put("baljubeonho", data.baljubeonho)
        values.put("nappumcheo", data.nappumcheo)
        values.put("georaecheomyeong", data.georaecheomyeong)
        values.put("baljuil", data.baljuil)
        values.put("nappumjangso", data.nappumjangso)
        values.put("resultmsg", data.resultmsg)


        db.insert("orderdetail", null, values)

        deleteBaljuDetail()

        for (balju_detail in data.returnBaljudetail()) {
            insertBaljuDetail(balju_detail)

            Log.d("??????DB???????????????????????????", balju_detail.pummyeong.toString())
        }


    }

    @SuppressLint("Range")
    fun getSavedOrderDetail(): ArrayList<OrderDetailResponse> {
        val list = ArrayList<OrderDetailResponse>()

        val query = "SELECT * FROM orderdetail;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {

            val orderDetail = OrderDetailResponse(
                c.getString(c.getColumnIndex("georaecheocode")),
                c.getString(c.getColumnIndex("bigo")),
                c.getString(c.getColumnIndex("resultcd")),
                c.getString(c.getColumnIndex("requesttype")),
                c.getString(c.getColumnIndex("baljubeonho")),
                c.getString(c.getColumnIndex("nappumcheo")),
                c.getString(c.getColumnIndex("georaecheomyeong")),
                c.getString(c.getColumnIndex("baljuil")),
                ArrayList(),
                c.getString(c.getColumnIndex("nappumjangso")),
                c.getString(c.getColumnIndex("resultmsg"))
            )

            orderDetail.baljudetail?.addAll(getAllSavedBaljuDetail())


            Log.d("?????????????????????????????????", orderDetail.returnBaljudetail().size.toString())
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
        values.put("seq", data.seq)
        values.put("pummokcode", data.pummokcode)
        values.put("pummyeong", data.pummyeong)
        values.put("dobeon_model", data.dobeon_model)
        values.put("sayang", data.sayang)
        values.put("balhudanwi", data.balhudanwi)
        values.put("baljusuryang", data.baljusuryang)
        values.put("ipgoyejeongil", data.ipgoyejeongil)
        values.put("giipgosuryang", data.giipgosuryang)
        values.put("ipgosuryang", data.ipgosuryang)
        values.put("jungyojajeyeobu", data.jungyojajeyeobu)
        values.put("location", data.location)


        db.insert("baljudetail", null, values)

    }

    @SuppressLint("Range")
    fun getAllSavedBaljuDetail(): ArrayList<Baljudetail> {
        val list = ArrayList<Baljudetail>()

        val query = "SELECT * FROM baljudetail;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {

            Log.d("?????????????????????", c.getString(c.getColumnIndex("pummyeong")))
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


    fun deleteLoginWorkCommon() {
        val query = "DELETE FROM LOGIN_WORK_COMMON;"
        db.execSQL(query)
    }

    fun insertLoginWorkCommon(
        userId: String,
        userPw: String,
        data: BasicResponse
    ) {
        val values = ContentValues()
        values.put("USERID", userId)
        values.put("USERNAME", data.sawonmyeong)
        values.put("USERPW", userPw)
        values.put("SAEOPJANGCODE", data.saeopjangcode)
        values.put("SAEOPJANGMYEONG", data.saeopjangmyeong)
        values.put("BUSEOCODE", data.buseocode)
        values.put("BUSEOMYEONG", data.buseomyeong)
        values.put("CHANGGOCODE", data.changgocode)
        values.put("CHANGGOMYEONG", data.changgomyeong)
        values.put("SECURITY_TOKEN", data.security_token)
        values.put("PROGRAM_VERSION", data.program_version)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        values.put("INPUTDATE", simpleDateFormat.format(Calendar.getInstance().time))

        values.put("WORKGUBUN", "None")
        values.put("WORKNUMBER", "None")
        values.put("TABLETIPNUMBER", IPUtil.getIpAddress())
        values.put("WORK_DATE", "TEMP")
        values.put("WORK_STATE", "000")

        db.insert("LOGIN_WORK_COMMON", null, values)

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
        serial: String,
        position: String,
    ) {
        val values = ContentValues()
        values.put("pummokcode", pummokcode)
        values.put("serial", serial)
        values.put("position", position)

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


    fun deleteBaljuCommon() {
        val query = "DELETE FROM BALJU_COMMON;"
        db.execSQL(query)
    }

    fun insertBaljuCommon(
        BALJUILJASTART: String?,
        BALJUILJAEND: String?,
        GEORAECHEOMEONG: String?,
        BALJUBEONHO: String?,
    ) {
        val values = ContentValues()
        values.put("BALJUILJASTART", BALJUILJASTART)
        values.put("BALJUILJAEND", BALJUILJAEND)
        values.put("GEORAECHEOMEONG", GEORAECHEOMEONG)
        values.put("BALJUBEONHO", BALJUBEONHO)

        db.insert("BALJU_COMMON", null, values)

    }

    @SuppressLint("Range")
    fun getAllBaljuCommon(): ArrayList<BaljuCommonLocalDB> {
        val list = ArrayList<BaljuCommonLocalDB>()

        val query = "SELECT * FROM BALJU_COMMON;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            list.add(
                BaljuCommonLocalDB(
                    c.getString(c.getColumnIndex("BALJUILJASTART")),
                    c.getString(c.getColumnIndex("BALJUILJAEND")),
                    c.getString(c.getColumnIndex("GEORAECHEOMEONG")),
                    c.getString(c.getColumnIndex("BALJUBEONHO")),
                )
            )

        }

        return list
    }


    fun deleteBaljuDetailInfo() {
        val query = "DELETE FROM BALJU_DETAIL_INFO;"
        db.execSQL(query)
    }

    fun insertBaljuDetailInfo(
        IPGODATE: String?,
        IPGOSAUPJANGCODE: String?,
        IPGOCHANGGOCODE: String?,
        IPGODAMDANGJA: String?,
    ) {
        val values = ContentValues()
        values.put("IPGODATE", IPGODATE)
        values.put("IPGOSAUPJANGCODE", IPGOSAUPJANGCODE)
        values.put("IPGOCHANGGOCODE", IPGOCHANGGOCODE)
        values.put("IPGODAMDANGJA", IPGODAMDANGJA)

        db.insert("BALJU_DETAIL_INFO", null, values)

    }

    @SuppressLint("Range")
    fun getAllBaljuDetailInfo(): ArrayList<BaljuDetailInfoLocalDB> {
        val list = ArrayList<BaljuDetailInfoLocalDB>()

        val query = "SELECT * FROM BALJU_DETAIL_INFO;"
        val c = db.rawQuery(query, null)
        while (c.moveToNext()) {
            list.add(
                BaljuDetailInfoLocalDB(
                    c.getString(c.getColumnIndex("IPGODATE")),
                    c.getString(c.getColumnIndex("IPGOSAUPJANGCODE")),
                    c.getString(c.getColumnIndex("IPGOCHANGGOCODE")),
                    c.getString(c.getColumnIndex("IPGODAMDANGJA")),
                )
            )

        }

        return list
    }


    fun updateWorkInfo(workType: String, workSEQ: String, status: String) {
        val query =
            "UPDATE LOGIN_WORK_COMMON SET WORKGUBUN='${workType}', WORKNUMBER = '${workSEQ}', WORK_STATE = '${status}'"
        db.execSQL(query)
    }


}