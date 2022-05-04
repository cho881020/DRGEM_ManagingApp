package kr.co.drgem.managingapp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.adapters.FindOrderListAdapter
import kr.co.drgem.managingapp.databinding.ActivityCompanyBinding
import kr.co.drgem.managingapp.fragments.OrderDetailDialog
import kr.co.drgem.managingapp.fragments.TradingStatementDialog

class CompanyActivity : BaseActivity() {

    lateinit var binding: ActivityCompanyBinding
    lateinit var mOrderListAdapter: FindOrderListAdapter

    val dialog = OrderDetailDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnDetail.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog_detail")
        }

    }

    override fun setValues() {

        mOrderListAdapter = FindOrderListAdapter()
        binding.findOrderRecyclerView.adapter = mOrderListAdapter

    }
}