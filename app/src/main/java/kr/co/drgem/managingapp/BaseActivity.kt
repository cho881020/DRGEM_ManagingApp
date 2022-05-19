package kr.co.drgem.managingapp

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

    lateinit var apiList : APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this




    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun backDialog(){

        androidx.appcompat.app.AlertDialog.Builder(mContext)
            .setTitle("취소하시겠습니까?")
            .setMessage("취소하면 변경하신 사항이 저장되지 않습니다.")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            .setNegativeButton("아니오", null)
            .show()

    }




}