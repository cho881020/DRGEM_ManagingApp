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
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationActivity : BaseActivity() {

    lateinit var binding: ActivityLocationBinding
    lateinit var mAdapter: LocationListAdapter
    lateinit var mList: ArrayList<Pummokdetail>

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
            saveDialog(){
                postRequestLocationAdd()
            }
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

        val spinnerAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, changgoList)
        binding.spinnerChanggocode.adapter = spinnerAdapter

        binding.spinnerChanggocode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

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

    fun getRequestLocation() {
        binding.btnFind.setOnClickListener {

            val changgocode = ""
            val location = binding.edtLocation.text.toString()
            inputPummyeong = binding.pummyeong.text.toString()



            apiList.getRequestLocation("02081", changgocode, location, inputPummyeong)
                .enqueue(object : Callback<LocationResponse> {
                    override fun onResponse(
                        call: Call<LocationResponse>,
                        response: Response<LocationResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                if (it.returnPummokDetail().size == 0) {
                                    Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT)
                                        .show()
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

    fun postRequestLocationAdd() {

        val pummokdetail: ArrayList<LocationPummokdetail> = arrayListOf()

        mList.forEach {

            var txtLocation = it.getLocationAdd()

            if (txtLocation.isNullOrEmpty()) {
                txtLocation = ""
            }else{
                pummokdetail.add(LocationPummokdetail(it.getPummokcodeHP(), it.getLocationAdd()))
            }
        }

        val locationAdd = LocationAdd(
            "02082", pummokdetail.size.toString(), "TEMP_SEQ", "777", pummokdetail
        )


        Log.d("yj", "로케이션등록 맵확인 : $locationAdd")

        if (pummokdetail.size > 0) {
            apiList.postRequestLocation(locationAdd).enqueue(object : Callback<WorkResponse> {
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.resultcd == "000") {


                                Toast.makeText(mContext, "저장이 완료되었습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            Log.d("yj", "로케이션등록 콜 결과코드 : ${it.resultcd}")
                            Log.d("yj", "로케이션등록 콜 결과메시지 : ${it.resultmsg}")
                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "로케이션등록 실패 결과메시지 : ${t.message}")
                }

            })
        }


    }

}