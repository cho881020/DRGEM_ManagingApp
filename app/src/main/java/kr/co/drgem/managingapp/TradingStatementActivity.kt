package kr.co.drgem.managingapp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.adapters.TradingStatementListAdapter
import kr.co.drgem.managingapp.databinding.ActivityTradingStatementBinding
import kr.co.drgem.managingapp.fragments.TradingStatementDialog

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