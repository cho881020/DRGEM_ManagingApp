package kr.co.drgem.managingapp

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.roomdb.DatabaseHelper

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

    lateinit var apiList: APIList

    lateinit var roomDB: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        roomDB = DatabaseHelper.getInstance(mContext)

    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun backDialog() {

        androidx.appcompat.app.AlertDialog.Builder(mContext)
            .setTitle("아직 저장하지 않은 사항이 있습니다.")
            .setMessage("그래도 이 화면을 종료하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            .setNegativeButton("아니오", null)
            .show()

    }


}