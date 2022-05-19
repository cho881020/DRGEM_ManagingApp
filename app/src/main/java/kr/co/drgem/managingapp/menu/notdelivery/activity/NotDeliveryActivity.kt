package kr.co.drgem.managingapp.menu.notdelivery.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityNotDeliveryBinding
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.adapter.NotDeliveryListAdapter
import kr.co.drgem.managingapp.menu.notdelivery.dialog.NotDeliveryDialog
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener

class NotDeliveryActivity : BaseActivity(), NotDeliveryEditListener {

    lateinit var binding: ActivityNotDeliveryBinding
    lateinit var mAdapter: NotDeliveryListAdapter
    val dialog = NotDeliveryDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)

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

    }

    override fun setValues() {

        mAdapter = NotDeliveryListAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "dialog_notDelivery")
    }
}