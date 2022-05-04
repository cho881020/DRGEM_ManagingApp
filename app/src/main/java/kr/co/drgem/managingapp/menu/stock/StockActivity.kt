package kr.co.drgem.managingapp.menu.stock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityStockBinding

class StockActivity : AppCompatActivity() {

    lateinit var binding : ActivityStockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)
    }
}