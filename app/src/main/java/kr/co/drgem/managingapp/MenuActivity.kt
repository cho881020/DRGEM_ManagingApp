package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMenuBinding

class MenuActivity : BaseActivity() {

    lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.tradingStatement.setOnClickListener{
            val myIntent = Intent(this, TradingStatementActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {

    }
}