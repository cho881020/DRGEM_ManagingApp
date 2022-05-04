package kr.co.drgem.managingapp.menu.tradingstatement.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.tradingstatement.adapter.TradingStatementAddListAdapter
import kr.co.drgem.managingapp.databinding.DialogAddTradingStatementBinding

class TradingStatementDialog : DialogFragment() {

    lateinit var binding : DialogAddTradingStatementBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_trading_statement, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()

    }


    fun setupEvents(){

        val mAdapter = TradingStatementAddListAdapter()
        binding.tradingStatementAddRecyclerView.adapter = mAdapter
    }

}