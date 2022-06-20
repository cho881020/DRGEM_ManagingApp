package kr.co.drgem.managingapp.menu.location.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityLocationBinding
import kr.co.drgem.managingapp.menu.location.adapter.LocationListAdapter
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationActivity : BaseActivity() {

    lateinit var binding: ActivityLocationBinding
    lateinit var mAdapter: LocationListAdapter
    lateinit var mList: ArrayList<Pummokdetail>
    val loadingDialog = LoadingDialogFragment()

    var changgocode = ""
    var inputPummyeong = ""
    var SEQ = ""
    var status = "111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)


        setupEvents()

    }

    override fun onBackPressed() {
        backDialog() {
            workStatusCancle()
        }
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog() {
                workStatusCancle()
            }
        }

        binding.btnSave.setOnClickListener {
            saveDialog() {
                postRequestLocationAdd()
            }
        }

        binding.btnLocationRemove.setOnClickListener {
            binding.edtLocation.text = null
        }

        binding.btnProductRemove.setOnClickListener {
            binding.pummyeong.text = null
        }

        val changgoList = ArrayList<Detailcode>()
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

        binding.btnFind.setOnClickListener {
            requestWorkseq()
        }

    }

    override fun setValues() {

        mAdapter = LocationListAdapter(mList)
        binding.recyclerView.adapter = mAdapter
//        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.locationProduct, "$inputPummyeong"), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.txtCount.text = "(${mList.size}건)"

    }

    fun requestWorkseq() {
        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "06",
            "tablet_ip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "status" to "111",
        )

        Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")


        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq
                            status = "333"

                            getRequestLocation()

                            Log.d("yj", "SEQ : ${it.seq}")
                        } else {
                            Log.d("yj", "SEQ 실패 코드 : ${it.resultmsg}")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                Log.d("yj", "SEQ 서버 실패 : ${t.message}")
            }

        })

    }

    fun getRequestLocation() {
        loadingDialog.show(supportFragmentManager, null)

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

                            mList = it.returnPummokDetail()

                            if (it.returnPummokDetail().size == 0) {
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                                mList.clear()

                            } else {
                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false
                            }

                            setValues()

                        }
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                    Toast.makeText(mContext, "${t.message}", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                    mList.clear()
                }
            })
    }

    fun postRequestLocationAdd() {

        loadingDialog.show(supportFragmentManager, null)

        val pummokdetail = JsonArray()

        mList.forEach {

            var txtLocation = it.getLocationAdd()

            if (txtLocation.isNullOrEmpty()) {
                txtLocation = ""
            } else {
                pummokdetail.add(
                    LocationPummokdetail(
                        it.getPummokcodeHP(),
                        it.getLocationAdd()
                    ).toJsonObject()
                )
            }
        }

        val locationAdd = hashMapOf(
            "requesttype" to "02082",
            "pummokcount" to pummokdetail.size().toString(),
            "seq" to SEQ,
            "status" to "777",
            "pummokdetail" to pummokdetail
        )


        Log.d("yj", "로케이션등록 맵확인 : $locationAdd")

        if (pummokdetail.size() > 0) {
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
                                getRequestLocation()

                            } else {
                                Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Toast.makeText(mContext, "${t.message}", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                }

            })
        }


    }

    fun workStatusCancle() {

        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val workCancelMap = hashMapOf(
            "requesttype" to "08002",
            "seq" to SEQ,
            "tablet_ip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "status" to status,
        )

        apiList.postRequestWorkstatusCancle(workCancelMap)
            .enqueue(object : Callback<WorkResponse> {
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {

                            Log.d("yj", "거래 작업상태취소 code : ${it.resultcd}")
                            Log.d("yj", "거래 작업상태취소 msg : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "발주 작업상태취소 실패 : ${t.message}")
                }

            })

    }

}