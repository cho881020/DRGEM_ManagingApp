package kr.co.drgem.managingapp.menu.tradingstatement.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityTradingStatementBinding
import kr.co.drgem.managingapp.menu.tradingstatement.dialog.TradingStatementDialog
import kr.co.drgem.managingapp.menu.tradingstatement.adapter.TradingStatementListAdapter

class TradingStatementActivity : BaseActivity() {

    lateinit var binding: ActivityTradingStatementBinding
    lateinit var mAdapter : TradingStatementListAdapter

    val dialog = TradingStatementDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trading_statement)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnAdd.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    override fun setValues() {

        mAdapter = TradingStatementListAdapter()
        binding.tradingStatementRecyclerView.adapter = mAdapter



    }
}