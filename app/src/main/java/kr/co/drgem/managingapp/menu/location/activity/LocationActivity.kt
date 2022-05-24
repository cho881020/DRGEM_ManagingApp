package kr.co.drgem.managingapp.menu.location.activity

import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding
import kr.co.drgem.managingapp.menu.location.adapter.LocationListAdapter

class LocationActivity : BaseActivity() {

    lateinit var binding: ActivityLocationBinding
    lateinit var mAdapter: LocationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)

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

        binding.btnLocationRemove.setOnClickListener {
            binding.edtLocation.text = null
        }

        binding.btnProductRemove.setOnClickListener {
            binding.edtProduct.text = null
        }


        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.locationProduct, "BUCKY_COVER_SILK_PRINT"), HtmlCompat.FROM_HTML_MODE_LEGACY)

    }

    override fun setValues() {

        mAdapter = LocationListAdapter()
        binding.recyclerView.adapter = mAdapter

    }
}