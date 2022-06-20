package kr.co.drgem.managingapp.menu.request.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityRequestDetailBinding
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.adapter.RequestDetailListAdapter
import kr.co.drgem.managingapp.menu.request.dialog.RequestDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RequestDetailActivity : BaseActivity(), RequestDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityRequestDetailBinding
    lateinit var mAdapter: RequestDetailListAdapter
    lateinit var requestDetailData: RequestDetailResponse

    val dialog = RequestDetailDialog()
    val loadingDialog = LoadingDialogFragment()

    var mYocheongbeonho = ""
    var SEQ = ""
    var status = "111"

    var johoejogeon = "0"
    var migwanri = "0"
    var companyCode = ""    // 조회에서 가져온 회사/창고코드
    var wareHouseCode = ""

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail)

        mYocheongbeonho = intent.getStringExtra("yocheongbeonho").toString()
        binding.yocheongbeonho.text = "요청번호 - $mYocheongbeonho"


        setupEvents()
        dateSet()
        PostRequestRequest()
        sort()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog() {
                workStatusCancle()
            }
        }

        binding.btnFind.setOnClickListener {
            requestWorkseq()
        }

    }

    fun setTempData(): TempData {
        var sawonCode = ""

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        val tempData = TempData(
            companyCodeOut,
            wareHouseCodeOut,
            mYocheongbeonho,
            SEQ,
            IPUtil.getIpAddress(),
            sawonCode
        )

        return tempData

    }

    override fun setValues() {

        mAdapter = RequestDetailListAdapter(this)
        mAdapter.setTemp(setTempData())
        mAdapter.setList(requestDetailData.returnPummokDetail())
        binding.recyclerView.adapter = mAdapter


        binding.txtCount.text = "(${requestDetailData.pummokcount}건)"

        requestDetailData.returnPummokDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }
        spinnerSetOut()
        spinnerSetIn()

    }

    fun requestWorkseq() {
        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "05",
            "tablet_ip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "status" to "111",
        )



        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq
                            status = "333"

                            getRequestDetail()

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

    fun getRequestDetail() {

        loadingDialog.show(supportFragmentManager, null)

        companyCode = intent.getStringExtra("companyCode").toString()
        wareHouseCode = intent.getStringExtra("wareHouseCode").toString()

        apiList.getRequestRequestDetail(
            "02062",
            mYocheongbeonho,
            johoejogeon,
            migwanri,
            companyCode,
            wareHouseCode
        ).enqueue(object : Callback<RequestDetailResponse> {
            override fun onResponse(
                call: Call<RequestDetailResponse>,
                response: Response<RequestDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        requestDetailData = it

                        setValues()

                    }
                }
                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<RequestDetailResponse>, t: Throwable) {
                Toast.makeText(mContext, "${t.message}", Toast.LENGTH_SHORT)
                loadingDialog.dismiss()

            }

        })

    }

    fun PostRequestRequest() {

        binding.btnSave.setOnClickListener {
            saveDialog() {

                loadingDialog.show(supportFragmentManager, null)
                val chulgodamdangjacode = binding.edtOutName.text.toString()
                val ipgodamdangjacode = binding.edtInName.text.toString()

                val requestChulgodetail = JsonArray()

                requestDetailData.returnPummokDetail().forEach {

                    var serialData =
                        SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                            .toString()

                    if (serialData == "null") {
                        serialData = ""
                    }


                    if (it.jungyojajeyeobu == "Y") {

                        if (serialData.isNotEmpty()) {        // 시리얼 데이터가 null아닐때만
                            val serialSize = serialData.split(",").size

                            if (serialSize.toString() != it.getSerialCount()) {
                                Toast.makeText(
                                    mContext,
                                    "입력 수량과 시리얼넘버 수량이 일치하지 않습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                it.serialCheck = true
                                mAdapter.notifyDataSetChanged()
                                serialData = ""

                                return@saveDialog

                            } else {
                                it.serialCheck = false
                                mAdapter.notifyDataSetChanged()
                            }
                        }

                    }

                    requestChulgodetail.add(
                        RequestChulgodetail(        //check : 요청번호?
                            it.getPummokcodeHP(),
                            serialData.split(",").size.toString(),
                            it.getjungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()
                    )

                }

                val requestAdd = hashMapOf(
                    "requesttype" to "02063",
                    "yocheongbeonho" to mYocheongbeonho,
                    "chulgoilja" to calDate,
                    "chulgosaupjangcode" to companyCodeOut,
                    "chulgochanggocode" to wareHouseCodeOut,
                    "chulgodamdangjacode" to chulgodamdangjacode,
                    "ipgosaupjangcode" to companyCodeIn,
                    "ipgochanggocode" to wareHouseCodeIn,
                    "ipgodamdangjacode" to ipgodamdangjacode,
                    "seq" to SEQ,
                    "status" to "777",
                    "pummokcount" to requestChulgodetail.size().toString(),
                    "chulgodetail" to requestChulgodetail
                )

                Log.d("yj", "requestAdd : $requestAdd")

                if (requestChulgodetail.size() > 0) {
                    apiList.postRequestRequestDelivery(requestAdd)
                        .enqueue(object : Callback<WorkResponse> {
                            override fun onResponse(
                                call: Call<WorkResponse>,
                                response: Response<WorkResponse>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {

                                            SerialManageUtil.clearData()
                                            getRequestDetail()

                                            Toast.makeText(
                                                mContext,
                                                "저장이 완료되었습니다.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        } else {
                                            Toast.makeText(
                                                mContext,
                                                it.resultmsg,
                                                Toast.LENGTH_SHORT
                                            ).show()
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
                } else {
                    Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
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


                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "발주 작업상태취소 실패 : ${t.message}")
                }

            })

    }


    fun spinnerSetOut() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyOut.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseOut.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeOut = "0001"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)
                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeOut = "0002"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGumiCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)

                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }
                        }

                        mAdapter.setTemp(setTempData())
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeOut = mWareHouseListOut[position].code
                        mAdapter.setTemp(setTempData())

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    fun spinnerSetIn() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyIn.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseIn.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeIn = "0001"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)
                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeIn = "0002"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGumiCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)

                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeIn = mWareHouseListIn[position].code

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    fun sort() {

        var onClickLocation = 0

        binding.layoutLocation.setOnClickListener {

            if (onClickLocation < 2) {
                onClickLocation++
            } else {
                onClickLocation = 0
            }

            when (onClickLocation) {

                0 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(requestDetailData.returnPummokDetail())
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(requestDetailData.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(requestDetailData.getUpLocation())
                }
            }

        }
    }


    fun dateSet() {
        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        calDate = dateServer.format(cal.time)
        binding.layoutDate.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calDate = dateServer.format(cal.time)
                    binding.txtDate.text = dateFormat.format(cal.time)

                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()
        }

    }


    override fun onClickedEdit(count: Int, data: Pummokdetail) {
        dialog.setCount(mYocheongbeonho, count, data)
        dialog.show(supportFragmentManager, "dialog_request")
    }

    override fun onBackPressed() {
        backDialog() {
            workStatusCancle()
        }
    }

    override fun onDismiss(p0: DialogInterface?) {

        mAdapter.notifyDataSetChanged()
    }

}