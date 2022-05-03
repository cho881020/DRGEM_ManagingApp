package kr.co.drgem.managingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityRequestBinding

class RequestActivity : AppCompatActivity() {

    lateinit var binding : ActivityRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request)
    }
}