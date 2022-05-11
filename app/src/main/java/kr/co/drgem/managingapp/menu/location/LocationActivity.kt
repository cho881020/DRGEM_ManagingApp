package kr.co.drgem.managingapp.menu.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding

class LocationActivity : BaseActivity() {

    lateinit var binding : ActivityLocationBinding

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
    }

    override fun setValues() {

    }
}