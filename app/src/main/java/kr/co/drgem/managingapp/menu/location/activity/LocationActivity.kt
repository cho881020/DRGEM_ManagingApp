package kr.co.drgem.managingapp.menu.location.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding
import kr.co.drgem.managingapp.menu.location.adapter.LocationListAdapter
import kr.co.drgem.managingapp.models.LocationResponse
import kr.co.drgem.managingapp.models.Pummokdetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationActivity : BaseActivity() {

    lateinit var binding: ActivityLocationBinding
    lateinit var mAdapter: LocationListAdapter
    lateinit var mList : ArrayList<Pummokdetail>

    var inputPummyeong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)

        getRequestLocation()
        setupEvents()

    }

    override fun onBackPressed() {
        backDialog()
    }
    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog()
        }

        binding.btnSave.setOnClickListener {
            saveDialog()
        }

        binding.btnLocationRemove.setOnClickListener {
            binding.edtLocation.text = null
        }

        binding.btnProductRemove.setOnClickListener {
            binding.pummyeong.text = null
        }




    }

    override fun setValues() {

        mAdapter = LocationListAdapter(mList)
        binding.recyclerView.adapter = mAdapter
        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.locationProduct, "$inputPummyeong"), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.txtCount.text = "(${mList.size}건)"

    }

    fun getRequestLocation(){
        binding.btnFind.setOnClickListener {

            val changgocode = ""
            val location = binding.edtLocation.text.toString()
            inputPummyeong = binding.pummyeong.text.toString()



            apiList.getRequestLocation("02081", changgocode, location, inputPummyeong).enqueue(object : Callback<LocationResponse>{
                override fun onResponse(
                    call: Call<LocationResponse>,
                    response: Response<LocationResponse>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            if (it.returnPummokDetail().size == 0) {
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                mList = it.returnPummokDetail()

                                setValues()

                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {

                }

            })

        }

    }

}