/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : LocationActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 로케이션조회 화면으로 로케이션요청 및 로케이션등록 기능
 */

package kr.co.drgem.managingapp.menu.location.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
    lateinit var locationData: LocationResponse
    val loadingDialog = LoadingDialogFragment()

    var changgocode = ""
    var inputPummyeong = ""
    var SEQ = ""
    var status = "111"
    var sawonCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)


        setupEvents()
        sort()

    }

    override fun onBackPressed() {
        if(status=="333"){
            backDialog() {
                workStatusCancle()
            }
        }
        else {
            finish()
        }

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            if(status=="333"){
                backDialog() {
                    workStatusCancle()
                }
            }
            else {
                finish()
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
            if(status=="111"){
                requestWorkseq()
            }
            else if (status == "333"){
                status333Dialog(){
                    requestWorkseq()
                }
            }
        }

    }

    override fun setValues() {

        mAdapter = LocationListAdapter()
        mAdapter.setList(locationData.returnPummokDetail())
        binding.recyclerView.adapter = mAdapter
//        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.locationProduct, "$inputPummyeong"), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.txtCount.text = "(${locationData.returnPummokDetail().size}건)"

        binding.btnSave.isVisible = true
    }

    fun sort() {

        var onClickPummyeong = 0

        binding.layoutPummyeong.setOnClickListener {

            if (onClickPummyeong < 2) {
                onClickPummyeong++
            } else {
                onClickPummyeong = 0
            }

            when (onClickPummyeong) {

                0 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(locationData.returnPummokDetail())
                }

                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(locationData.getDownPummyeong())
                }

                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(locationData.getUpPummyeong())
                }
            }

        }


    }



    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

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
    //    로케이션요청
    fun getRequestLocation() {

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

                            locationData = it

                            if (it.returnPummokDetail().size == 0) {
                                searchZeroDialog()
                                mAdapter.clearList()

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
                    serverErrorDialog("서버 연결에 실패하였습니다.\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                    mAdapter.clearList()
                }
            })
    }
    //    로케이션등록
    fun postRequestLocationAdd() {
        val pummokdetail = JsonArray()

        locationData.returnPummokDetail().forEach {

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


                                saveDoneDialog()

                            } else {
                                serverErrorDialog(it.resultmsg)
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    serverErrorDialog("서버 연결에 실패하였습니다.\n 관리자에게 문의하세요.")
                }

            })
        }


    }
    //    작업상태취소
    fun workStatusCancle() {

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