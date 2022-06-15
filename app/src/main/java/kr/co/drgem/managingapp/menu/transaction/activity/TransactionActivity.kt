package kr.co.drgem.managingapp.menu.transaction.activity

import android.app.AlertDialog
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

    val dialogEdit = TransactionDialog()

    var SEQ = ""
    var status = "111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)


        setupEvents()
        sort()


    }

    override fun setupEvents() {


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

            backDialog(){
                workStatusCancle()
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

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnSave.setOnClickListener {
            saveDialog() {
                postRequestTran()
            }
        }

        binding.btnFind.setOnClickListener {
            requestWorkseq()
        }

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
        binding.recyclerView.adapter = mAdapter


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

                        if (mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }

                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                        companyCode = "0002"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGumiCode())

                        if (mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }

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

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


    }

    fun requestWorkseq() {
        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
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
                            status = "333"

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


                            if (it.returnGeoraedetail().size == 0) {
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {

                                setValues()
                                mAdapter.setList(it.returnGeoraedetail())

                                binding.layoutEmpty.isVisible = false
                                binding.layoutList.isVisible = true
                                binding.layoutInfo.isVisible = true
                                binding.layoutFold.isVisible = true
                                binding.btnSave.isVisible = true

                            }

                        }
                    }
                }

                override fun onFailure(call: Call<TranResponse>, t: Throwable) {
                    Log.d("yj", "거래명세요청실패 : ${t.message}")
                }

            })
    }


    fun postRequestTran() {

        val georaedetail = JsonArray()   // 등록용 리스트
        val inputName = binding.edtName.text.toString()


        tranData.returnGeoraedetail().forEach {

            var serialData = SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                .toString()      // 거래명세번호 내의 품목코드(키) 값으로 시리얼 데이터 꺼내오기

            Log.d("yj", "serialData : $serialData")

            if (serialData.isEmpty()) {        // 시리얼 데이터가 빈 값일 경우

                serialData = ""             // "" 으로 표시

            }

            val serialCount = serialData.split(",").size.toString()


            if (serialData != "null") {

                georaedetail.add(                         // 리스트에 담기
                    GeoraedetailAdd(
                        it.getSeqHP(),
                        it.getPummokcodeHP(),
                        serialCount,
                        it.getJungyojajeyeobuHP(),
                        it.getBaljubeonhoHP(),
                        serialData
                    ).toJsonObject()                            // JSONObject로 제작
                )
            }


        }

        val georaeMap = hashMapOf(
            "requesttype" to "02002",
            "georaemyeongsebeonho" to tranData.getGeoraemyeongsebeonhoHP(),
            "georaecheocode" to tranData.getGeoraecheocodeHP(),
            "ipgoilja" to calDate,
            "ipgosaupjangcode" to companyCode,
            "ipgochanggocode" to wareHouseCode,
            "ipgodamdangja" to inputName,
            "seq" to SEQ, // TODO - SEQ 관련 API 연동 성공시 수정해야함
            "status" to "777",
            "pummokcount" to georaedetail.size().toString(),
            "georaedetail" to georaedetail
        )

        Log.d("yj", "거래명세등록 맵확인 : $georaeMap")

        if (georaedetail.size() > 0) {
            apiList.postRequestTranDetail(georaeMap).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.resultcd == "000") {

                                SerialManageUtil.clearData()
                                mAdapter.notifyDataSetChanged()

                                Toast.makeText(mContext, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT).show()
                            }

                            Log.d("yj", "거래명세등록 콜 결과코드 : ${it.resultcd}")
                            Log.d("yj", "거래명세등록 콜 결과메시지 : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        } else {
            Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
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
    }


    override fun onClickedEdit(count: Int, data: Georaedetail) {
        dialogEdit.show(supportFragmentManager, "dialog")
        dialogEdit.setCount(count, data)

    }


    override fun onBackPressed() {

        backDialog(){
            workStatusCancle()
        }

    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()             // 어댑터 데이터 변경 (시리얼이 담긴 리스트 버튼 컬러 변경)
    }


}