/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingDetailActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 키팅출고 화면으로 키팅명세요청 및 일괄출고등록 기능
 */

package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
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

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityKittingDetailBinding
    lateinit var mAdapter: KittingDetailListAdapter
    lateinit var kittingDetailData: KittingDetailResponse

    val dialog = KittingDetailDialog()
    val loadingDialog = LoadingDialogFragment()

    var mkittingbeonho = ""
    var SEQ = ""
    var status = "111"
    var sawonCode = ""

    var johoejogeon = "0"
    var migwanri = "0"
    var changgocode = ""

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting_detail)

        mkittingbeonho = intent.getStringExtra("kittingbeonho").toString()
        binding.kittingbeonho.text = "키팅번호 - $mkittingbeonho"

        setupEvents()
        dateSet()
        postRequestKitting()
        sort()
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

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
            binding.btnFold.isVisible = false
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
            binding.btnFold.isVisible = true
        }



        binding.btnInNameRemove.setOnClickListener {
            binding.edtInName.text = null
        }

        binding.btnFind.setOnClickListener {
            requestWorkseq()
        }

        binding.radio0.setOnClickListener {
            johoejogeon = "0"
        }

        binding.radio1.setOnClickListener {
            johoejogeon = "1"
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                migwanri = "0"
            }else{
                migwanri = "1"
            }
        }

    }

    fun setTempData(): TempData {

        val tempData = TempData(
            companyCodeOut,
            wareHouseCodeOut,
            "",
            SEQ,
            IPUtil.getIpAddress(),
            sawonCode
        )

        return tempData

    }

    override fun setValues() {

        mAdapter = KittingDetailListAdapter(this)
        mAdapter.setTemp(setTempData())
        mAdapter.setList(kittingDetailData.returnKittingDetail())
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${kittingDetailData.getPummokCount()} 건)"
        binding.chulgodamdangjacode.text = sawonCode


        kittingDetailData.returnKittingDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

        spinnerSetOut()
        spinnerSetIn()

    }
    //    작업 SEQ 요청
    fun requestWorkseq() {

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

        Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")


        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq
                            status = "333"

                            getRequestKittingDetail()


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
    //    키팅명세요청
    fun getRequestKittingDetail() {

        loadingDialog.show(supportFragmentManager, null)

        Log.d("yj", "조회조건 : $johoejogeon , 미관리 :$migwanri")
        apiList.getRequestKittingDetail("02502", mkittingbeonho, johoejogeon, migwanri, changgocode)
            .enqueue(object : Callback<KittingDetailResponse> {
                override fun onResponse(
                    call: Call<KittingDetailResponse>,
                    response: Response<KittingDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            kittingDetailData = it
                            setValues()

                            if (it.returnKittingDetail().size == 0) {
                                searchZeroDialog()
                                mAdapter.clearList()

                            }
                        }
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                    loadingDialog.dismiss()
                    serverErrorDialog("서버 연결에 실패하였습니다.\n 관리자에게 문의하세요.")
                }

            })

    }
    //    일괄출고등록
    fun postRequestKitting() {

        binding.btnSave.setOnClickListener {
            saveDialog() {

                val chulgodetail = JsonArray()   // 등록용 리스트
                val ipgodamdangjacode = binding.edtInName.text.toString()

                kittingDetailData.returnKittingDetail().forEach {

                    if(it.getSerialCount() == "0" || it.getSerialCount() == null){
                        return@forEach
                    }

                    var serialData =
                        SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                            .toString()      // 시리얼 데이터 꺼내오기

                    if (it.jungyojajeyeobu == "Y") {
                            val serialSize = serialData.split(",").size

                            if (serialSize.toString() != it.getSerialCount()|| serialData == "null") {
                                countSerialDialog()
                                it.serialCheck = true
                                mAdapter.notifyDataSetChanged()
                                serialData = ""

                                return@saveDialog
                            } else {
                                it.serialCheck = false
                                mAdapter.notifyDataSetChanged()
                            }

                    }


                    chulgodetail.add(
                        KittingChulgodetail(
                            it.getyocheongbeonhoHP(),
                            it.getPummokcodeHP(),
                            it.getSerialCount(),
                            it.getjungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()
                    )
                }


                val kittingAdd = hashMapOf(
                    "requesttype" to "02053",
                    "kittingbeonho" to mkittingbeonho,
                    "chulgoilja" to calDate,
                    "chulgosaupjangcode" to companyCodeOut,
                    "chulgochanggocode" to wareHouseCodeOut,
                    "chulgodamdangjacode" to sawonCode,
                    "ipgosaupjangcode" to companyCodeIn,
                    "ipgodamdangjacode" to ipgodamdangjacode,
                    "ipgochanggocode" to wareHouseCodeIn,
                    "seq" to SEQ,
                    "status" to "777",
                    "pummokcount" to chulgodetail.size().toString(),
                    "chulgodetail" to chulgodetail
                )


                Log.d("yj", "일괄출고등록 맵확인 : $kittingAdd")

                if (chulgodetail.size() > 0) {
                    apiList.postRequestDeliveryBatch(kittingAdd)
                        .enqueue(object : Callback<WorkResponse> {
                            override fun onResponse(
                                call: Call<WorkResponse>,
                                response: Response<WorkResponse>
                            ) {

                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {

                                            SerialManageUtil.clearData()
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


                } else {
                    saveNotDoneDialog()
                }

            }
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
                        Log.d("yj", "wareHouseCodeIn : $wareHouseCodeIn")

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
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpLocation())
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

        dialog.setCount(mkittingbeonho, count, data)
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()             // 어댑터 데이터 변경 (시리얼이 담긴 리스트 버튼 컬러 변경)
    }
}