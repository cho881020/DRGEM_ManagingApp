package kr.co.drgem.managingapp.menu.request.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.request.adapter.RequestListAdapter
import kr.co.drgem.managingapp.databinding.ActivityRequestBinding

class RequestActivity : BaseActivity() {

    lateinit var binding : ActivityRequestBinding

    lateinit var mAdapter : RequestListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request)

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

        mAdapter = RequestListAdapter()
        binding.findRequestList.adapter = mAdapter

    }
}