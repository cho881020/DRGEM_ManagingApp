package kr.co.drgem.managingapp.menu.order.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderDetailBinding
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener {

    lateinit var binding : ActivityOrderDetailBinding
    lateinit var mAdapter : OrderDetailListAdapter
    val dialog = OrderDetailDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_detail)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {


        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

    }

    override fun setValues() {

        mAdapter = OrderDetailListAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "EditDialog")
    }
}