/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : TransactionActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 거래명세입고 화면으로 거래명세요청, 거래명세등록 기능
 */

package kr.co.drgem.managingapp.menu.transaction.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityTransactionBinding
import kr.co.drgem.managingapp.menu.transaction.adapter.TransactionAdapter
import kr.co.drgem.managingapp.menu.transaction.dialog.TransactionDialog
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : BaseActivity(), transactionEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter: TransactionAdapter
    lateinit var detailCode: Detailcode

    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var companyCode = "0001"
    var wareHouseCode = "1001"
    var calDate = ""
    lateinit var tranData: TranResponse

    val loadingDialog = LoadingDialogFragment()

    var SEQ = ""
    var status = "111"
    var sawonCode = ""

    var baljubeonho = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }
        binding.ipgodamdangja.text = sawonCode


        setupEvents()
        sort()
        postRequestTran()

        spinnerSet()

    }

    override fun setupEvents() {

        binding.edtTranNum.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.btnFind.performClick()
                }
            }
            return@setOnEditorActionListener actionId != 5
        }

        binding.layoutBigo.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("비고 내용 전체")
                .setMessage(tranData.getBigoHP())
                .setNegativeButton("확인", null)
                .show()
        }


        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
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


        binding.btnBack.setOnClickListener {

            if(status=="333"){
                backDialog() {
                    SerialManageUtil.clearData()
                    workStatusCancle()
                }
            }
            else{
                finish()
            }

        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
        }

        binding.btnTranRemove.setOnClickListener {
            binding.edtTranNum.text = null
            binding.layoutInfo.isVisible = false
        }


        binding.btnFind.setOnClickListener {

            if(status=="111"){
                requestWorkseq()
            }
            else if (status == "333"){
                status333Dialog(){
                    SerialManageUtil.clearData()
                    requestWorkseq()
                }
            }
        }

    }

    fun setTempData(): TempData {

        val tempData = TempData(
            companyCode,
            wareHouseCode,
            baljubeonho,
            SEQ,
            IPUtil.getIpAddress(),
            sawonCode
        )

        return tempData

    }

    override fun setValues() {

        binding.georaecheomyeong.text = tranData.getGeoraecheomyeongHP()
        binding.nappumcheomyeong.text = tranData.getNappumcheomyeongHP()
        binding.bigo.text = tranData.getBigoHP()
        binding.txtCount.text = "(${tranData.returnGeoraedetail().size}건)"



        tranData.returnGeoraedetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }


        mAdapter = TransactionAdapter(this)
        mAdapter.setList(tranData.returnGeoraedetail())
        mAdapter.setTemp(setTempData())
        binding.recyclerView.adapter = mAdapter




    }


    fun spinnerSet() {

        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(
                mContext,
                R.layout.spinner_list_item,
                masterData.getCompanyCode()
            )
        binding.spinnerCompany.adapter = spinnerCompanyAdapter


        val spinnerWareHouseAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
        binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

        binding.spinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                        companyCode = "0001"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGwangmyeongCode())
                        binding.spinnerWareHouse.setSelection(0, false)

                        if (mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }


                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                        companyCode = "0002"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGumiCode())
                        binding.spinnerWareHouse.setSelection(0, false)

                        if (mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }

                    }
                    try{
                        mAdapter.setTemp(setTempData())
                    }catch (e: Exception){

                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        binding.spinnerWareHouse.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {

                    wareHouseCode = mWareHouseList[position].code

                    try{
                        mAdapter.setTemp(setTempData())
                    }catch (e: Exception){

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


    }

    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "01",
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
                            getRequestTran()

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

    //    거래명세조회
    fun getRequestTran() {

        val inputNum = binding.edtTranNum.text.toString()


        apiList.getRequestTranDetail("02001", inputNum)
            .enqueue(object : Callback<TranResponse> {
                override fun onResponse(
                    call: Call<TranResponse>,
                    response: Response<TranResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {

                            tranData = it
                            baljubeonho = it.getGeoraemyeongsebeonhoHP()
                            setValues()

                            if (it.returnGeoraedetail().size == 0) {

                                searchZeroDialog()
                                mAdapter.clearList()
                                binding.txtCount.text = "(0건)"
                                binding.layoutInfo.isVisible = false
                                status = "111"


                            } else {
                                status = "333"
                                binding.layoutEmpty.isVisible = false
                                binding.layoutList.isVisible = true
                                binding.layoutInfo.isVisible = true
                                binding.layoutFold.isVisible = true
                                binding.btnSave.isVisible = true

                            }
                        }
                        loadingDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<TranResponse>, t: Throwable) {

                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                }

            })
    }


//    거래명세등록
    fun postRequestTran() {

        binding.btnSave.setOnClickListener {

            tranData.returnGeoraedetail().forEach {

                if (it.getPummokCount() == "0" || it.getPummokCount() == null) {
                    return@forEach
                }

                var serialData =
                    SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                        .toString()      // 거래명세번호 내의 품목코드(키) 값으로 시리얼 데이터 꺼내오기


                if (it.getJungyojajeyeobuHP() == "Y") {
                    val serialSize = serialData.trim().split(",").size

                    if (serialSize.toString() != it.getPummokCount() || serialData == "null") {
                        countSerialDialog()
                        it.serialCheck = true
                        mAdapter.notifyDataSetChanged()
                        serialData = ""
                        return@setOnClickListener

                    } else {
                        it.serialCheck = false
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            saveDialog() {
                val georaedetail = JsonArray()   // 등록용 리스트

                tranData.returnGeoraedetail().forEach {

                    if (it.getPummokCount() == "0" || it.getPummokCount() == null) {
                        return@forEach
                    }

                    val serialData =
                        SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                            .toString()      // 거래명세번호 내의 품목코드(키) 값으로 시리얼 데이터 꺼내오기


//                    if (it.getJungyojajeyeobuHP() == "Y") {
//                        val serialSize = serialData.trim().split(",").size
//
//                        if (serialSize.toString() != it.getSerialCount() || serialData == "null") {
//                            countSerialDialog()
//                            it.serialCheck = true
//                            mAdapter.notifyDataSetChanged()
//                            serialData = ""
//                            return@saveDialog
//
//                        } else {
//                            it.serialCheck = false
//                            mAdapter.notifyDataSetChanged()
//                        }
//                    }


                    georaedetail.add(                         // 리스트에 담기
                        GeoraedetailAdd(
                            it.getBaljubeonhoHP(),
                            it.getSeqHP(),
                            it.getPummokcodeHP(),
                            it.getPummokCount(),
                            it.getJungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()                            // JSONObject로 제작
                    )

                }

                val georaeMap = hashMapOf(
                    "requesttype" to "02002",
                    "georaemyeongsebeonho" to tranData.getGeoraemyeongsebeonhoHP(),
                    "georaecheocode" to tranData.getGeoraecheocodeHP(),
                    "ipgoilja" to calDate,
                    "ipgosaupjangcode" to companyCode,
                    "ipgochanggocode" to wareHouseCode,
                    "ipgodamdangja" to sawonCode,
                    "seq" to SEQ,
                    "status" to "777",
                    "pummokcount" to georaedetail.size().toString(),
                    "georaedetail" to georaedetail
                )

                Log.d("yj", "거래명세등록 맵확인 : $georaeMap")

                if (georaedetail.size() > 0) {
                    loadingDialog.show(supportFragmentManager, null)
                    apiList.postRequestTranDetail(georaeMap)
                        .enqueue(object : Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {

                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {


                                            status = "111"
                                            SerialManageUtil.clearData()
                                            mAdapter.clearList()
                                            saveDoneDialog()

                                        } else {
                                            serverErrorDialog("${it.resultmsg}")
                                        }

                                    }
                                }
                                loadingDialog.dismiss()
                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                                Log.d("yj", "거래명세서실패 : ${t.message}")
                                loadingDialog.dismiss()
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


    fun sort() {

        var onClickSeq = 0

        binding.layoutSeq.setOnClickListener {

            if (onClickSeq < 2) {
                onClickSeq++
            } else {
                onClickSeq = 0
            }

            when (onClickSeq) {

                0 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(tranData.returnGeoraedetail())

                }

                1 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(tranData.getDownSeq())

                }

                2 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropup)
                    mAdapter.setList(tranData.getUpSeq())
                }
            }

        }

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
                    mAdapter.setList(tranData.returnGeoraedetail())
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(tranData.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(tranData.getUpLocation())
                }
            }

        }



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
                    mAdapter.setList(tranData.returnGeoraedetail())
                }

                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(tranData.getDownPummyeong())
                }

                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(tranData.getUpPummyeong())
                }
            }

        }
    }


    override fun onClickedEdit(data: Georaedetail) {

        val dialogEdit = TransactionDialog()
        dialogEdit.show(supportFragmentManager, "dialog")
        dialogEdit.setCount(data, setTempData())

    }


    override fun onBackPressed() {

        if(status=="333"){
            backDialog() {
                SerialManageUtil.clearData()
                workStatusCancle()
            }
        }
        else {
            finish()
        }


    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()             // 어댑터 데이터 변경 (시리얼이 담긴 리스트 버튼 컬러 변경)
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }


}