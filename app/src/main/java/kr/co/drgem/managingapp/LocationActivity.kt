package kr.co.drgem.managingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {

    lateinit var binding : ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_location)
    }
}