package kr.co.drgem.managingapp.menu.order.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderBinding
import kr.co.drgem.managingapp.menu.order.adapter.OrderListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog

class OrderActivity : BaseActivity() {

    lateinit var binding : ActivityOrderBinding
    lateinit var mOrderAdapter : OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnFind.setOnClickListener {
            binding.layoutList.isVisible = true
            binding.layoutEmpty.isVisible = false
        }

        binding.btnCompanyRemove.setOnClickListener {
            binding.edtCompany.text = null
        }

        binding.btnOrderRemove.setOnClickListener {
            binding.edtOrder.text = null
        }



    }

    override fun setValues() {
        mOrderAdapter = OrderListAdapter()
        binding.recyclerView.adapter = mOrderAdapter
    }
}