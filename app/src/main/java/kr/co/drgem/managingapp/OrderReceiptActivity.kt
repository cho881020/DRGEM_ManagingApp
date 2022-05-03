package kr.co.drgem.managingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityOrderReceiptBinding

class OrderReceiptActivity : AppCompatActivity() {

    lateinit var binding : ActivityOrderReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_receipt)
    }
}