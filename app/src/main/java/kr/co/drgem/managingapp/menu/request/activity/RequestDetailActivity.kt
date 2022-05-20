package kr.co.drgem.managingapp.menu.request.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityRequestDetailBinding
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.adapter.RequestDetailListAdapter
import kr.co.drgem.managingapp.menu.request.dialog.RequestDetailDialog

class RequestDetailActivity : BaseActivity(), RequestDetailEditListener {

    lateinit var binding: ActivityRequestDetailBinding
    lateinit var mAdapter: RequestDetailListAdapter
    val dialog = RequestDetailDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

       binding.btnBack.setOnClickListener {
           finish()
       }


    }

    override fun setValues() {

        mAdapter = RequestDetailListAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "dialog_request")
    }
}