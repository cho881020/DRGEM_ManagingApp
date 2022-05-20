package kr.co.drgem.managingapp.menu.kitting.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener {

    lateinit var binding : ActivityKittingDetailBinding
    lateinit var mAdapter : KittingDetailListAdapter
    val dialog = KittingDetailDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_kitting_detail)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnOutNameRemove.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInNameRemove.setOnClickListener {
            binding.edtInName.text = null
        }

    }

    override fun setValues() {
        mAdapter = KittingDetailListAdapter(this)
        binding.recyclerView.adapter = mAdapter



    }

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }
}