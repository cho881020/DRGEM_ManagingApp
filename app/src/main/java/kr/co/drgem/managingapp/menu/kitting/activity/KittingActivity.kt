package kr.co.drgem.managingapp.menu.kitting.activity

import android.os.Bundle
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

    }

    override fun setValues() {

        mAdapter = KittingListAdapter()
        binding.findKittingList.adapter = mAdapter
    }
}