package kr.co.drgem.managingapp.menu.location.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding
import kr.co.drgem.managingapp.menu.location.adapter.LocationListAdapter
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.LocationPummokdetail
import kr.co.drgem.managingapp.models.LocationResponse
import kr.co.drgem.managingapp.models.Pummokdetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationActivity : BaseActivity() {

    lateinit var binding: ActivityLocationBinding
    lateinit var mAdapter: LocationListAdapter
    lateinit var mList : ArrayList<Pummokdetail>

    var changgocode = ""
    var inputPummyeong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)

        getRequestLocation()
        setupEvents()

    }

    override fun onBackPressed() {
        backDialog(null)
    }
    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
        }

        binding.btnSave.setOnClickListener {
            saveDialog(null)
        }

        binding.btnLocationRemove.setOnClickListener {
            binding.edtLocation.text = null
        }

        binding.btnProductRemove.setOnClickListener {
            binding.pummyeong.text = null
        }

        val changgoList = java.util.ArrayList<Detailcode>()
        changgoList.add(Detailcode("", "전체"))
        changgoList.add(Detailcode("2001", "자재창고1"))
        changgoList.add(Detailcode("2014", "자재창고2"))

        val spinnerAdapter = MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, changgoList)
        binding.spinnerChanggocode.adapter = spinnerAdapter

        binding.spinnerChanggocode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {

                changgocode = changgoList[position].code
                Log.d("yj", "창고코드 : $changgocode")

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


    }

    override fun setValues() {

        mAdapter = LocationListAdapter(mList)
        binding.recyclerView.adapter = mAdapter
//        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.locationProduct, "$inputPummyeong"), HtmlCompat.FROM_HTML_MODE_LEGACY)
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

    fun postRequestLocationAdd(){

        mList.forEach {
            val pummokdetail : ArrayList<LocationPummokdetail> = arrayListOf()


        }




    }

}