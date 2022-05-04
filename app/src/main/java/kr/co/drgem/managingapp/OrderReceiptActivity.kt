package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.adapters.FindCompanyListAdapter
import kr.co.drgem.managingapp.databinding.ActivityOrderReceiptBinding

class OrderReceiptActivity : BaseActivity() {

    lateinit var binding : ActivityOrderReceiptBinding
    lateinit var mFindCompanyAdapter : FindCompanyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_receipt)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnDetail.setOnClickListener {
            val myIntent = Intent(this, CompanyActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mFindCompanyAdapter = FindCompanyListAdapter()
        binding.findCompanyList.adapter = mFindCompanyAdapter
    }
}