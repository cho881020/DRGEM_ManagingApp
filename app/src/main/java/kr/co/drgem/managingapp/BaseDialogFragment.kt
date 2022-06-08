package kr.co.drgem.managingapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.localdb.DBHelper
import kr.co.drgem.managingapp.localdb.SQLiteDB

abstract class BaseDialogFragment : DialogFragment() {

    lateinit var mContext: Context

    lateinit var apiList: APIList

    lateinit var mSqliteDB : SQLiteDB



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        val dbHelper = DBHelper(mContext, "drgemdb.db", null, 1)
        mSqliteDB = SQLiteDB()
        mSqliteDB.makeDb(dbHelper.writableDatabase)


    }

    abstract fun setupEvents()

    abstract fun setValues()


}