package kr.co.drgem.managingapp.menu.stock.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityStockBinding
import kr.co.drgem.managingapp.menu.stock.adapter.StockListAdapter

class StockActivity : BaseActivity() {

    lateinit var binding: ActivityStockBinding
    lateinit var mAdapter: StockListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

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

        mAdapter = StockListAdapter()
        binding.recyclerView.adapter = mAdapter

    }
}