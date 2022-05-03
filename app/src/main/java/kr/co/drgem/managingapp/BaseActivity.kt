package kr.co.drgem.managingapp

import android.content.Context
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


}