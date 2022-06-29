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
import android.widget.ArrayAdapter
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
import kr.co.drgem.managingapp.utils.*
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityKittingDetailBinding
    lateinit var mAdapter: KittingDetailListAdapter
    lateinit var kittingDetailData: KittingDetailResponse

    val loadingDialog = LoadingDialogFragment()

    var mkittingbeonho = ""
    var SEQ = ""
    var status = "111"

    var sawonData = ArrayList<SawonData>()
    var sawonCode = ""

    var johoejogeon = "0"
    var migwanri = "0"
    var changgocode = ""
    var ipgodamdangjacode = ""

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

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }
        binding.chulgodamdangjacode.text = sawonCode

        setupEvents()
        dateSet()
        postRequestKitting()
        sort()

        spinnerSetOut()
        spinnerSetIn()

        completeTextView()
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

    override fun setupEvents() {


        binding.btnBack.setOnClickListener {
            if(status=="333"){
                backDialog() {
                    workStatusCancle()
                    SerialManageUtil.clearData()
                }
            }
            else{
                finish()
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



        binding.btnFind.setOnClickListener {
            if(status=="111"){
                requestWorkseq()
            }
            else if (status == "333"){
                status333Dialog(){
                    requestWorkseq()
                    SerialManageUtil.clearData()
                }
            }
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


//        kittingDetailData.returnKittingDetail().forEach {
//            if (it.jungyojajeyeobu == "Y") {
//                binding.serialDetail.isVisible = true
//            }
//        }

        binding.btnSave.isVisible = true

    }
    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "03",
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

                            status = "333"

                            if (it.returnKittingDetail().size == 0) {
                                searchZeroDialog()
                                mAdapter.clearList()

                                status = "111"

                            }
                        }
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                    loadingDialog.dismiss()
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                }

            })

    }
    //    일괄출고등록
    fun postRequestKitting() {

        binding.btnSave.setOnClickListener {

//                사원코드 가져오기
            val selecSawon = binding.autoCompleteTextView.text.toString()
            sawonData.forEach {
                if(it.sawonmyeong == selecSawon){
                    ipgodamdangjacode = it.sawoncode
                    Log.d("yj", "사원명 : $selecSawon , 사원코드 : ${it.sawoncode}")
                }
            }

            if(ipgodamdangjacode == ""){
                ipgodamdangjacodeDialog()
                return@setOnClickListener
            }

            kittingDetailData.returnKittingDetail().forEach {

                if (it.getPummokCount() == "0" || it.getPummokCount() == null) {  // 출고수량이 0일때는 체크없음
                    return@forEach
                }

                var serialData =
                    SerialManageUtil.getSerialStringByPummokCode("${it.getPummokcodeHP()}/${it.getyocheongbeonhoHP()}")
                        .toString()      // 시리얼 데이터 꺼내오기

                if (it.jungyojajeyeobu == "Y") {
                    val serialSize = serialData.split(",").size

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

                val chulgodetail = JsonArray()   // 등록용 리스트

                kittingDetailData.returnKittingDetail().forEach {

                    if(it.getPummokCount() == "0" || it.getPummokCount() == null){  // 출고수량이 0일때는 저장하지 않음
                        return@forEach
                    }

                    val serialData =
                        SerialManageUtil.getSerialStringByPummokCode("${it.getPummokcodeHP()}/${it.getyocheongbeonhoHP()}")
                            .toString()      // 시리얼 데이터 꺼내오기
//
//                    if (it.jungyojajeyeobu == "Y") {
//                            val serialSize = serialData.split(",").size
//
//                            if (serialSize.toString() != it.getSerialCount()|| serialData == "null") {
//                                countSerialDialog()
//                                it.serialCheck = true
//                                mAdapter.notifyDataSetChanged()
//                                serialData = ""
//
//                                return@saveDialog
//                            } else {
//                                it.serialCheck = false
//                                mAdapter.notifyDataSetChanged()
//                            }
//
//                    }


                    chulgodetail.add(
                        KittingChulgodetail(
                            it.getyocheongbeonhoHP(),
                            it.getPummokcodeHP(),
                            it.getPummokCount(),
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

                                            status = "111"
                                            SerialManageUtil.clearData()
                                            mAdapter.clearList()
                                            saveDoneDialog()

                                        } else {
                                            serverErrorDialog(it.resultmsg)
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
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

                        try{
                            mAdapter.setTemp(setTempData())
                        }catch (e: Exception){

                        }
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
                        try{
                            mAdapter.setTemp(setTempData())
                        }catch (e: Exception){

                        }

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
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }

                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownPummyeong())
                }

                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpPummyeong())
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


    private fun completeTextView(){

        val sawonmyeongList = ArrayList<String>()

        SawonDataManager.getSawonData()?.let{
            sawonData = it.sawon
        }

        sawonData.forEach {
            sawonmyeongList.add(it.sawonmyeong)
        }

        val autoCompleteTextView = binding.autoCompleteTextView

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sawonmyeongList)

        autoCompleteTextView.setAdapter(adapter)

    }


    override fun onClickedEdit(data: Pummokdetail) {
        val dialog = KittingDetailDialog()
        dialog.setCount(mkittingbeonho,data, setTempData())
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()             // 어댑터 데이터 변경 (시리얼이 담긴 리스트 버튼 컬러 변경)
    }
}