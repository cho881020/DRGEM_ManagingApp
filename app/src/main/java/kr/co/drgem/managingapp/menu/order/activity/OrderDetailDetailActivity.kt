package kr.co.drgem.managingapp.menu.order.activity

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
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityOrderDetailBinding
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityOrderDetailBinding
    lateinit var mAdapter: OrderDetailListAdapter
    lateinit var orderDetailData: OrderDetailResponse

    val dialog = OrderDetailDialog()
    lateinit var masterData: MasterDataResponse
    val baljuDetail = ArrayList<Baljudetail>()
    var mBaljubeonho = ""
    var SEQ = ""
    var status = "333"

    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var companyCode = "0001"
    var wareHouseCode = "1001"
    var calDate = ""

    var sawonCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)

        mBaljubeonho = intent.getStringExtra("baljubeonho").toString()
        SEQ = intent.getStringExtra("seq").toString()


        setupEvents()
        setValues()
        spinnerSet()
        getRequestOrderDetail()

    }

    override fun onBackPressed() {
        backDialog {
            clearAndCancelWork()
            workStatusCancle()
        }
    }

    override fun setupEvents() {

        binding.btnTempSave.setOnClickListener {

            clearAndSaveDataToDB()
        }


        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnBack.setOnClickListener {
            backDialog {
                clearAndCancelWork()
                workStatusCancle()
            }
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

        binding.btnSave.setOnClickListener {

            saveDialog() {
                postRequestOrderDetail()
            }

        }
    }

    override fun setValues() {

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        val tempData = TempData(companyCode, wareHouseCode, mBaljubeonho, SEQ, IPUtil.getIpAddress(), sawonCode)

        mAdapter = OrderDetailListAdapter(this, mContext, baljuDetail, tempData)
        binding.recyclerView.adapter = mAdapter


    }

    fun spinnerSet() {
        masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

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

    fun setOrderDetailDataToUI() {

        binding.baljubeonho.text = "발주번호 - $mBaljubeonho"
        binding.baljubeonho2.text = mBaljubeonho

        binding.baljuil.text = orderDetailData.getBaljuilHP()
        binding.georaecheocode.text = orderDetailData.getGeoraecheocodeHP()
        binding.georaecheomyeong.text = orderDetailData.getGeoraecheomyeongHP()
        binding.bigo.text = orderDetailData.getBigoHP()
        binding.txtCount.text = "(${baljuDetail.size}건)"

        baljuDetail.forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

        SerialManageUtil.clearData()

        for (pummok in baljuDetail) {
            val serialList = mSqliteDB.getAllSerialByPummokcode(pummok.getPummokcodeHP())


            val contentString = StringBuilder()
            for (data in serialList) {

                if (data.serial.isNotBlank()) {
                    contentString.append("${data.serial},")
                }

            }
            if (contentString.length > 1) {
                contentString.setLength(contentString.length - 1)
                SerialManageUtil.putSerialStringByPummokCode(
                    pummok.getPummokcodeHP(),
                    contentString.toString()
                )
            }


        }

        mAdapter.notifyDataSetChanged()

    }

    fun getRequestOrderDetail() {

        val savedOrderDetailList = mSqliteDB.getSavedOrderDetail()
        if (savedOrderDetailList.size > 0 && savedOrderDetailList[0].baljubeonho == mBaljubeonho) {
            orderDetailData = savedOrderDetailList[0]
            baljuDetail.clear()

            for (detail in orderDetailData.returnBaljudetail()) {
                baljuDetail.add(detail)
            }

            val baljuDetailInfoLocalDB = mSqliteDB.getAllBaljuDetailInfo()[0]

            var companyIndex = 0
            masterData.getCompanyCode().forEachIndexed { index, company ->
                if (company.code == baljuDetailInfoLocalDB.IPGOSAUPJANGCODE) {
                    companyIndex = index
                }
            }

            binding.spinnerCompany.setSelection(companyIndex)

            var wareHouseIndex = 0

            mWareHouseList.forEachIndexed { index, wareHouse ->

                if (wareHouse.code == baljuDetailInfoLocalDB.IPGOCHANGGOCODE) {
                    wareHouseIndex = index
                }

            }

            binding.spinnerWareHouse.setSelection(wareHouseIndex)


            binding.edtName.setText(baljuDetailInfoLocalDB.IPGODAMDANGJA)

            setOrderDetailDataToUI()
        } else {
            apiList.getRequestOrderDetail("02012", mBaljubeonho)
                .enqueue(object : Callback<OrderDetailResponse> {
                    override fun onResponse(
                        call: Call<OrderDetailResponse>,
                        response: Response<OrderDetailResponse>
                    ) {

                        response.body()?.let {

                            orderDetailData = it

                            baljuDetail.clear()
                            baljuDetail.addAll(it.returnBaljudetail())

                            setOrderDetailDataToUI()


                            clearAndSaveDataToDB()

                        }
                    }

                    override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {

                        Log.d("yj", "OrderDetail 실패 : ${t.message}")
                    }

                })
        }


    }

    fun postRequestOrderDetail() {
        val ipgodetail = JsonArray()   // 등록용 리스트
        val inputName = binding.edtName.text.toString()


        orderDetailData.returnBaljudetail().forEach {

            var serialData =
                SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP()).toString()

            Log.d("yj", "serialData : $serialData")

            if (serialData.isEmpty()) {        // null값 까지 같이 들어옴

                serialData = ""

            }

            if (serialData != "null") {
                ipgodetail.add(
                    IpgodetaildetailAdd(
                        it.getSeqHP(),
                        it.getPummokcodeHP(),
                        serialData.split(",").size.toString(),
                        it.getJungyojajeyeobuHP(),
                        serialData
                    ).toJsonObject()
                )
            }

        }

        val georaeMap = hashMapOf(
            "requesttype" to "02014",
            "baljubeonho" to mBaljubeonho,
            "ipgoilja" to calDate,
            "ipgosaupjangcode" to companyCode,
            "ipgochanggocode" to wareHouseCode,
            "ipgodamdangja" to inputName,
            "georaecheocode" to orderDetailData.getGeoraecheocodeHP(),
            "seq" to SEQ, // TODO - SEQ 관련 API 연동 성공시 수정해야함
            "status" to "777",
            "pummokcount" to ipgodetail.size().toString(),
            "ipgodetail" to ipgodetail
        )

        Log.d("yj", "georaeMap : ${georaeMap}")
        Log.d("yj", "발주입고등록SEQ : $SEQ")
        Log.d("yj", "ipgodetail : ${Gson().toJson(ipgodetail)}")

        if (ipgodetail.size() > 0) {
            apiList.postRequestOrderReceive(georaeMap).enqueue(object : Callback<WorkResponse> {
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
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

                            Log.d("yj", "요청명세등록 콜 결과코드 : ${it.resultcd}")
                            Log.d("yj", "요청명세등록 콜 결과메시지 : ${it.resultmsg}")
                            Log.d("yj", "요청명세등록 콜  : ${it}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "요청명세등록 실패메시지 : ${t.message}")
                }
            })
        } else {
            Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun workStatusCancle() {

        // TODO - API 정상 연동시 수정
        val workCancelMap = hashMapOf(
            "requesttype" to "",
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

                            Log.d("yj", "발주 작업상태취소 code : ${it.resultcd}")
                            Log.d("yj", "발주 작업상태취소 msg : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "발주 작업상태취소 실패 : ${t.message}")
                }

            })

    }


    fun clearAndCancelWork() {

        Log.d("KJ", "DB에 있는 작업 데이터 삭제만 하기")
        mSqliteDB.deleteOrderDetail()
        mSqliteDB.deleteAllSerials()
    }

    fun clearAndSaveDataToDB() {

        mSqliteDB.deleteBaljuDetailInfo()

        mSqliteDB.deleteOrderDetail()
        mSqliteDB.deleteAllSerials()

        mSqliteDB.insertBaljuDetailInfo(

            binding.txtDate.text.toString(),
            masterData.getCompanyCode()[binding.spinnerCompany.selectedItemPosition].code,
            mWareHouseList[binding.spinnerCompany.selectedItemPosition].code,
            binding.edtName.text.toString()

        )

        mSqliteDB.insertOrderDetail(orderDetailData)

//        Toast.makeText(mContext, "임시 저장이 완료 되었습니다.", Toast.LENGTH_SHORT).show()

    }

    override fun onClickedEdit(count: Int, data: Baljudetail) {

        dialog.setCount(mBaljubeonho, count, data)
        dialog.show(supportFragmentManager, "EditDialog")
        supportFragmentManager.executePendingTransactions()


//        dialog.dialog?.setOnDismissListener(this)
    }

    override fun onDismiss(p0: DialogInterface?) {

        Log.d("다이얼로그닫히면", "로그찍히나")

        mAdapter.notifyDataSetChanged()
    }


}