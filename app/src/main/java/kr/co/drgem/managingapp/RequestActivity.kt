package kr.co.drgem.managingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.adapters.KittingListAdapter
import kr.co.drgem.managingapp.adapters.RequestListAdapter
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

    }

    override fun setValues() {

        mAdapter = RequestListAdapter()
        binding.findRequestList.adapter = mAdapter

    }
}