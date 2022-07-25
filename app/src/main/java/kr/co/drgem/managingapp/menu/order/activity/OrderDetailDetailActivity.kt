/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : OrderDetailDetailActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 매입입고 화면으로 발주명세요청 및 발주대비입고등록 기능
 */

package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityOrderDetailBinding
import kr.co.drgem.managingapp.localdb.model.BaljuInfoCommonDB
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog
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

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding        : ActivityOrderDetailBinding
    lateinit var mAdapter       : OrderDetailListAdapter
    lateinit var orderDetailData: OrderDetailResponse

    val loadingDialog = LoadingDialogFragment()
    lateinit var masterData: MasterDataResponse
    val baljuDetail   = ArrayList<Baljudetail>()
    var mBaljubeonho  = ""
    var SEQ           = ""
    var status        = "333"

    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var companyCode    = "0001"
    var wareHouseCode  = "1001"

    var companyCode0   = "0001"
    var wareHouseCode0 = "1001"
    var CompanySel     = 0
    var WareHouseSel   = 0
    var FirstSetSW     = 0    // 사업장코드와 창고코드 처음 한번 적용하기 위한 것
    var calDate        = ""

    var sawonCode      = ""

    // sort의 상태를 파악하기 위한 변수
    var onClickPummokcode  = 0
    var onClickPummyeong   = 0
    var onClickDobeonModel = 0
    var onClickSayang      = 0
    var onClickLocation    = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)

        mBaljubeonho = intent.getStringExtra("baljubeonho").toString()
        SEQ          = intent.getStringExtra("seq").toString()

        LoginUserUtil.getLoginData()?.let {
            sawonCode      = it.sawoncode.toString()
            companyCode0   = it.saeopjangcode.toString()  // by jung 2022.07.02
            wareHouseCode0 = it.changgocode.toString()    // by jung 2022.07.02
        }
        binding.ipgodamdangja.text = sawonCode

        MainDataManager.getMainData()?.let{
            masterData = it
        }

        setupEvents()  // 각종 이벤트 지정

        if (intent.getBooleanExtra("WorkRecovery", false)) {  // 이것은 복구시 작업
            setOrderDataByLocalDB()
        }
        else {  // 복구시 작업이 아닌 일상의 작업인 경우
            getRequestOrderDetail() // 서버로 발주명세요청
        }

        // 아래의 3개 문장도 복구시 관계 조사해 보아야 한다.
        postRequestOrderDetail()    // btnSave 키 이벤트에 의한 발주입고등록작업
        sort()                      // sort event 세팅
        spinnerSet()                // 콤보박스 세팅
    }

    // 시스템 종료키(태블릿 PC 아랫쪽 세모 버튼)를 누른 경우
    override fun onBackPressed() {
        if (status == "333") {
            backDialog() {
                clearAndCancelWork()         // 로컬테이블 상세정보, 시리얼정보 clear
                workStatusCancle()           // 서버에 취소 허가 받고, 상태정보 변경
                                             // WORKGUBUN="None",WORKNUMBER="None",WORK_STATE="000"
                SerialManageUtil.clearData() // 시리얼정보 clear
            }
        } else {
            finish()
        }
    }

    override fun setupEvents() {

        // 테스트용?? "저장하기" 버튼 앞쪽에 숨겨져 있음
        // 화면정의(xml)에서 android:visibility="gone" 문장을 빼면 보이고 실행 된다.
        binding.btnTempSave.setOnClickListener {
            clearAndSaveDataToDB()  // 로컬테이블의 발주상세공통정보1.발주디테일정보.시리얼정보 clear하고, 발주상세공통정보1. 발주상세정보 저장
        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
        }

        // 이전키를 누르면 상태가 333이면 상태 변경작업 진행
        binding.btnBack.setOnClickListener {

            if (status == "333") {
                backDialog() {
                    clearAndCancelWork()          // 로컬테이블 상세정보, 시리얼정보 clear
                    workStatusCancle()            // 서버에 취소 허가 받고, 상태정보 변경
                                                  // WORKGUBUN="None",WORKNUMBER="None",WORK_STATE="000"
                    SerialManageUtil.clearData()  // 시리얼정보 clear
                }
            } else {
                finish()
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
    }

    fun setTempData(): TempData {

        val tempData = TempData(
            companyCode,
            wareHouseCode,
            mBaljubeonho,
            SEQ,
            IPUtil.getIpAddress(),
            sawonCode
        )
        return tempData
    }

    override fun setValues() {

        mAdapter = OrderDetailListAdapter(this, mContext)
        mAdapter.setList(orderDetailData.returnBaljudetail())
        mAdapter.setTemp(setTempData())
        binding.recyclerView.adapter = mAdapter
    }

    fun spinnerSet() {
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

        if (FirstSetSW == 0) {
            var iCnt = binding.spinnerCompany.count
            for ( i: Int in 0 until iCnt) {
                if (masterData.getCompanyCode()[i].code == companyCode0){
                    CompanySel = i
                }
            }
            binding.spinnerCompany.setSelection(CompanySel)
        }

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

                        if (FirstSetSW == 0) {
                            var iCnt = binding.spinnerWareHouse.count
                            for ( i: Int in 0 until iCnt) {
                                if (masterData.getGwangmyeongCode()[i].code == wareHouseCode0){
                                    WareHouseSel = i
                                }
                            }
                            binding.spinnerWareHouse.setSelection(WareHouseSel,false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[WareHouseSel].code
                            }
                            FirstSetSW = 1
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

                        if (FirstSetSW == 0) {
                            var iCnt = binding.spinnerWareHouse.count
                            for ( i: Int in 0 until iCnt) {
                                if (masterData.getGumiCode()[i].code == wareHouseCode0){
                                    WareHouseSel = i
                                }
                            }
                            binding.spinnerWareHouse.setSelection(WareHouseSel,false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[WareHouseSel].code
                            }
                            FirstSetSW = 1
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

    fun sort() {

        // 품목코드 소트
        binding.layoutPummokcode.setOnClickListener {

            sortImageClear(1)

            if (onClickPummokcode < 2) {
                onClickPummokcode++
            } else {
                onClickPummokcode = 0
            }

            when (onClickPummokcode) {
                0 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(orderDetailData.returnBaljudetail())
                }
                1 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(orderDetailData.getDownPummokcode())
                }
                2 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropup)
                    mAdapter.setList(orderDetailData.getUpPummokcode())
                }
            }
        }

        // 품목명 소트
         binding.layoutPummyeong.setOnClickListener {

             sortImageClear(2)

             if (onClickPummyeong < 2) {
                onClickPummyeong++
            } else {
                onClickPummyeong = 0
            }

            when (onClickPummyeong) {
                0 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(orderDetailData.returnBaljudetail())
                }
                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(orderDetailData.getDownPummyeong())
                }
                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(orderDetailData.getUpPummyeong())
                }
            }
        }


        // 도번/모델 소트
        binding.layoutDobeonModel.setOnClickListener {

            sortImageClear(3)

            if (onClickDobeonModel < 2) {
                onClickDobeonModel++
            } else {
                onClickDobeonModel = 0
            }

            when (onClickDobeonModel) {
                0 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(orderDetailData.returnBaljudetail())
                }
                1 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(orderDetailData.getDownDobeonModel())
                }
                2 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropup)
                    mAdapter.setList(orderDetailData.getUpDobeonModel())
                }
            }
        }

        // 사양 소트
        binding.layoutSayang.setOnClickListener {

            sortImageClear(4)

            if (onClickSayang < 2) {
                onClickSayang++
            } else {
                onClickSayang = 0
            }

            when (onClickSayang) {
                0 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(orderDetailData.returnBaljudetail())
                }
                1 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(orderDetailData.getDownSayang())
                }
                2 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropup)
                    mAdapter.setList(orderDetailData.getUpSayang())
                }
            }
        }

        // 위치 소트
        binding.layoutLocation.setOnClickListener {

            sortImageClear(5)

            if (onClickLocation < 2) {
                onClickLocation++
            } else {
                onClickLocation = 0
            }

            when (onClickLocation) {
                0 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(orderDetailData.returnBaljudetail())
                }
                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(orderDetailData.getDownLocation())
                }
                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(orderDetailData.getUpLocation())
                }
            }
        }
    }

    // 다른 항목의 소트의 상태를 초기상태로 표시
    private fun sortImageClear(iP : Int){
        if (iP != 1){
            onClickPummokcode  = 0
            binding.imgPummokcode.setImageResource(R.drawable.dropempty)
        }
        if (iP != 2){
            onClickPummyeong   = 0
            binding.imgPummyeong.setImageResource(R.drawable.dropempty)
        }
        if (iP != 3){
            onClickDobeonModel = 0
            binding.imgDobeonModel.setImageResource(R.drawable.dropempty)
        }
        if (iP != 4){
            onClickSayang      = 0
            binding.imgSayang.setImageResource(R.drawable.dropempty)
        }
        if (iP != 5){
            onClickLocation    = 0
            binding.imgLocation.setImageResource(R.drawable.dropempty)
        }
    }

    // 발주번호에 따른 상세품목 정보를 디스플레이
    fun setOrderDetailDataToUI() {

        binding.baljubeonho     .text = "발주번호 - $mBaljubeonho"
        binding.baljubeonho2    .text = mBaljubeonho
        binding.baljuil         .text = orderDetailData.getBaljuilHP()
        binding.georaecheocode  .text = orderDetailData.getGeoraecheocodeHP()
        binding.georaecheomyeong.text = orderDetailData.getGeoraecheomyeongHP()
        binding.bigo            .text = orderDetailData.getBigoHP()
        binding.txtCount        .text = "(${baljuDetail.size}건)"

        baljuDetail.forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

        SerialManageUtil.clearData()  // 전체 시리얼정보를 Clear

        for (pummok in baljuDetail) {  // 시리얼 정보가 로컬테이블에 왜 존재하는지 ???
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

    // 발주명세요청
    fun getRequestOrderDetail() {
        loadingDialog.show(supportFragmentManager, null)
        apiList.getRequestOrderDetail("02012", mBaljubeonho)
            .enqueue(object : Callback<OrderDetailResponse> {
                override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: Response<OrderDetailResponse>
                ) {
                    response.body()?.let {

                        orderDetailData = it
                        setValues()  // 받은 데이터 디스플레이

                        baljuDetail.clear()
                        baljuDetail.addAll(it.returnBaljudetail())

                        setOrderDetailDataToUI()    // 발주번호에 따른 상세품목 정보를 디스플레이

                        clearAndSaveDataToDB()   // 로컬테이블의 발주상세공통정보1.발주디테일정보.시리얼정보 clear하고, 발주상세공통정보1. 발주상세정보 저장
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                }
            })
    }

//    // 현재 공통 정보와 디테일 정보가 분리된 상태이므로 전반적으로 재 작성 되어야 한다.
//    private fun setOrderDataByLocalDB() {
//
//        val savedOrderDetailList = mSqliteDB.getSavedOrderDetail()
//
//        orderDetailData = savedOrderDetailList[0]
//        setValues()
//
//        baljuDetail.clear()
//
//        for (detail in orderDetailData.returnBaljudetail()) {
//            baljuDetail.add(detail)
//        }
//
//        setSpinnerDataByLocalDB()
//    }

    // 상세디테일 화면에서의 복구작업 시작 루틴
    private fun setOrderDataByLocalDB() {

        val savedOrderDetailList = mSqliteDB.getSavedOrderDetail()

        // 시리얼번호 정보는 어디서 읽어 오는 가???? 지우지 않아서 그대로 남아 있다???
        // 그렇다면 시리얼정보 로컬테이블 읽어서 어디에 복사해야 하는 지 검사
        // 일단 시리얼정보 저장하는 곳을 찾아 볼 것
         SerialLocalDB.clear()
        // mSqliteDB.getAllSerialByPummokcode

        orderDetailData = savedOrderDetailList[0]
        setValues()

        baljuDetail.clear()

        for (detail in orderDetailData.returnBaljudetail()) {
            baljuDetail.add(detail)
        }

        setSpinnerDataByLocalDB()
    }

    // 로컬테이블 정보를 읽어서 사업장, 창고 세팅 후
    // 발주번호에 따른 상세품목 정보를 디스플레이
    private fun setSpinnerDataByLocalDB() {
        val baljuInfoCommonLocalDB = mSqliteDB.getAllBaljuInfoCommon()[0]  // 발주상세공통정보

        var companyIndex = 0
        masterData.getCompanyCode().forEachIndexed { index, company ->
            if (company.code == baljuInfoCommonLocalDB.IPGOSAUPJANGCODE) {
                companyIndex = index
            }
        }

        binding.spinnerCompany.setSelection(companyIndex)

        var wareHouseIndex = 0

        mWareHouseList.forEachIndexed { index, wareHouse ->

            if (wareHouse.code == baljuInfoCommonLocalDB.IPGOCHANGGOCODE) {
                wareHouseIndex = index
            }
        }

        binding.spinnerWareHouse.setSelection(wareHouseIndex)

        setOrderDetailDataToUI()    // 발주번호에 따른 상세품목 정보를 디스플레이
    }

    // 발주 입고등록
    fun postRequestOrderDetail() {

        // 저장하기 버튼 클릭시
        binding.btnSave.setOnClickListener {

            orderDetailData.returnBaljudetail().forEach {

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
                val ipgodetail = JsonArray()   // 등록용 리스트

                orderDetailData.returnBaljudetail().forEach {

                    if (it.getPummokCount() == "0" || it.getPummokCount() == null) {
                        return@forEach
                    }

                    val serialData =
                        SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                            .toString()

//                    if (it.jungyojajeyeobu == "Y") {
//
//                        val serialSize = serialData.split(",").size
//
//
//                        if (serialSize.toString() != it.getSerialCount() || serialData == "null") {
//                            countSerialDialog()
//                            it.serialCheck = true
//                            mAdapter.notifyDataSetChanged()
//                            serialData = ""
//
//                            return@saveDialog
//                        } else {
//                            it.serialCheck = false
//                            mAdapter.notifyDataSetChanged()
//                        }
//                    }

                    ipgodetail.add(
                        IpgodetaildetailAdd(
                            it.getSeqHP(),
                            it.getPummokcodeHP(),
                            it.getPummokCount(),
                            it.getJungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()
                    )
                }

                val georaeMap = hashMapOf(
                    "requesttype"      to "02014",
                    "baljubeonho"      to mBaljubeonho,
                    "ipgoilja"         to calDate,
                    "ipgosaupjangcode" to companyCode,
                    "ipgochanggocode"  to wareHouseCode,
                    "ipgodamdangja"    to sawonCode,
                    "georaecheocode"   to orderDetailData.getGeoraecheocodeHP(),
                    "seq"              to SEQ,
                    "status"           to "777",
                    "pummokcount"      to ipgodetail.size().toString(),
                    "ipgodetail"       to ipgodetail
                )

                Log.d("yj", "georaeMap     : ${georaeMap}")
                Log.d("yj", "발주입고등록SEQ : $SEQ")
                Log.d("yj", "ipgodetail    : ${Gson().toJson(ipgodetail)}")

                if (ipgodetail.size() > 0) {
                    loadingDialog.show(supportFragmentManager, null)
                    apiList.postRequestOrderReceive(georaeMap)
                        .enqueue(object : Callback<WorkResponse> {
                            override fun onResponse(
                                call: Call<WorkResponse>,
                                response: Response<WorkResponse>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {

                                            SerialManageUtil.clearData()
                                            mAdapter.clearList()
                                            saveDoneDialog()
                                            // 작업번호는 그대로 유지한채 상태코드만 111로 변경??
                                            // 더이상 작업 내용이 없으므로 이전 화면으로 돌아가야 한다. ??
                                            // 로컬테이블 정보 클리어 및 로컬테이블 상태정보 변경  // 작업 상태코드, SEQ 등
                                        } else {
                                            serverErrorDialog(it.resultmsg)
                                        }
                                    }
                                }
                                loadingDialog.dismiss()
                            }
                            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                                loadingDialog.dismiss()
                            }
                        })
                } else {
                    saveNotDoneDialog()
                }
            }
        }
    }

    // 작업상태취소
    fun workStatusCancle() {

        val workCancelMap = hashMapOf(
            "requesttype" to "08002",
            "seq"         to SEQ,
            "tablet_ip"   to IPUtil.getIpAddress(),
            "sawoncode"   to sawonCode,
            "status"      to status,
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

                            // "None" "None" "111" ??? 판단 필요
                            mSqliteDB.updateWorkInfo("None", "None", "000")
                        }
                    }
                }
                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "발주 작업상태취소 실패 : ${t.message}")
                }
            })
    }

    // 로컬테이블의 상세정보, 시리얼정보 삭제
    fun clearAndCancelWork() {
        Log.d("KJ", "DB에 있는 작업 데이터 삭제만 하기")
        mSqliteDB.deleteBaljuDetail()      // 발주디테일정보 삭제
        mSqliteDB.deleteAllSerials()       // 시리얼정보 삭제
    }

    // 로컬테이블의 발주상세공통정보1.발주디테일정보.시리얼정보 clear하고, 발주상세공통정보1. 발주상세정보 저장
    // 발주명세요청에서 정상적으로 데이터를 받고 처리한 다음 진행된다.
    fun clearAndSaveDataToDB() {
        clearAndCancelWork()  // 로컬테이블의 상세정보, 시리얼정보 삭제

// 테이블 통합전의 실행문 - 아래의 문장들로 대체 됨
//        mSqliteDB.deleteBaljuDetailInfo()  // 발주상세공통정보1 삭제
//        mSqliteDB.insertBaljuDetailInfo(   // 발주상세공통정보1 저장
//            binding.txtDate.text.toString(),                                               // 일자
//            masterData.getCompanyCode()[binding.spinnerCompany.selectedItemPosition].code, // 사업장코드
//            mWareHouseList[binding.spinnerCompany.selectedItemPosition].code,              // 창고코드
//            sawonCode                                                                      // 사원코드
//        )

        updateBaljuINfoCommWork()  // 현재의 공통정보(선택발주번호정보+전송공통)를 로컬테이블에 저장한다.

        mSqliteDB.insertOrderDetail(orderDetailData)  // 발주상세정보 저장
    }

    // OrderDetailListViewHolder.kt에서 btnEdit 클릭시 실행되는 수량.시리얼번호 입력 루틴
    override fun onClickedEdit(data: Baljudetail) {

        val dialog = OrderDetailDialog()
        dialog.setCount(mBaljubeonho, data, setTempData())
        dialog.show(supportFragmentManager, "EditDialog")
        supportFragmentManager.executePendingTransactions()

        updateBaljuINfoCommWork1()  // 여기서 무조건 현재의 공통정보를 로컬테이블에 저장한다. 세분화 구분 생략 (취소,등록)

        // dialog.dialog?.setOnDismissListener(this)
    }

    // 발주 공통정보 업데이트- 2군데에서 이루어 진다.// 선택발주번호상세정보 + 발주상세공통정보(전송데이터4개) 업데이트
    // 시리얼번호 입력화면(또는 수량입력화면)에서 등록하기(임시저장)에서는 전송데이터4개만 필요하지만 전체 업데이트 진행
    // 저장하기에서는 직접 화면의 데이터를 사용하기때문에 별도의 저장 불필요
    fun updateBaljuINfoCommWork(){
        mSqliteDB.updateBaljuInfoCommon(
            orderDetailData.baljubeonho,                                                   // 선택된 발주번호
            orderDetailData.baljuil,                                                       // 선택된 발주일
            orderDetailData.georaecheocode,                                                // 선택된 거래처코드
            orderDetailData.georaecheomyeong,                                              // 선택된 거래처명
            orderDetailData.bigo,                                                          // 선택된 비고
            orderDetailData.nappumjangso,                                                  // 선택된 납품장소
            binding.txtDate.text.toString(),                                               // 일자
            masterData.getCompanyCode()[binding.spinnerCompany.selectedItemPosition].code, // 사업장코드
            mWareHouseList[binding.spinnerCompany.selectedItemPosition].code,              // 창고코드
            sawonCode                                                                      // 사원코드
        )
    }
    // 발주 공통정보 업데이트- 2군데에서 이루어 진다.// 선택발주번호상세정보 + 발주상세공통정보(전송데이터4개) 업데이트
    // 시리얼번호 입력화면(또는 수량입력화면)에서 등록하기(임시저장)에서는 전송데이터4개만 업데이트 진행
    // 저장하기에서는 직접 화면의 데이터를 사용하기때문에 별도의 저장 불필요
    fun updateBaljuINfoCommWork1(){
        mSqliteDB.updateBaljuInfoCommon1(
            binding.txtDate.text.toString(),                                               // 일자
            masterData.getCompanyCode()[binding.spinnerCompany.selectedItemPosition].code, // 사업장코드
            mWareHouseList[binding.spinnerCompany.selectedItemPosition].code,              // 창고코드
            sawonCode                                                                      // 사원코드
        )
    }

    override fun onDismiss(p0: DialogInterface?) {

        Log.d("다이얼로그닫히면", "로그찍히나")

        mAdapter.notifyDataSetChanged()
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }
}