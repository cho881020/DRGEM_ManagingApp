package kr.co.drgem.managingapp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityTradingStatementBinding

class TradingStatementActivity : BaseActivity() {

    lateinit var binding: ActivityTradingStatementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trading_statement)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}