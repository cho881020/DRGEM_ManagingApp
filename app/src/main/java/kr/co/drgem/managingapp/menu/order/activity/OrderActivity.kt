package kr.co.drgem.managingapp.menu.order.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderBinding
import kr.co.drgem.managingapp.menu.order.adapter.FindCompanyListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog

class OrderActivity : BaseActivity() {

    lateinit var binding : ActivityOrderBinding
    lateinit var mFindCompanyAdapter : FindCompanyListAdapter
    val dialog = OrderDetailDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnDetail.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    override fun setValues() {
        mFindCompanyAdapter = FindCompanyListAdapter()
        binding.recyclerView.adapter = mFindCompanyAdapter
    }
}