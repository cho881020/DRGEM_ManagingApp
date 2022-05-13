package kr.co.drgem.managingapp.menu.kitting.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingListAdapter

class KittingActivity : BaseActivity() {

    lateinit var binding : ActivityKittingBinding
    lateinit var mAdapter : KittingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting)

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
            binding.edtWarehouse.text = null
        }

    }

    override fun setValues() {

        mAdapter = KittingListAdapter()
        binding.recyclerView.adapter = mAdapter
    }
}