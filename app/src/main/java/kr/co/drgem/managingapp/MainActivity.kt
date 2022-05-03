package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

       binding.btnLogin.setOnClickListener{
            val myIntent = Intent(this, MenuActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

    }


}