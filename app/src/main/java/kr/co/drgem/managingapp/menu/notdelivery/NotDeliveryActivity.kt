package kr.co.drgem.managingapp.menu.notdelivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityNotDeliveryBinding

class NotDeliveryActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)
    }
}