package kr.co.drgem.managingapp.menu.notdelivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityNotShippedBinding

class NotDeliveryActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotShippedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)
    }
}