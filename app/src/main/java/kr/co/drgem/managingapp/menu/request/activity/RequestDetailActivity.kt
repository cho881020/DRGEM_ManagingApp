/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : RequestDetailActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 자재출고 화면으로 요청명세요청 및 요청출고등록 기능
 */

package kr.co.drgem.managingapp.menu.request.activity

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
import kr.co.drgem.managingapp.databinding.ActivityRequestDetailBinding
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.adapter.RequestDetailListAdapter
import kr.co.drgem.managingapp.menu.request.dialog.RequestDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.*
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

    val loadingDialog     = LoadingDialogFragment()

    var mYocheongbeonho   = ""

    var SEQ               = ""
    var status            = "111"

    var sawonCode         = ""
    var sawonData         = ArrayList<SawonData>()
    var ipgodamdangjacode = ""

    var johoejogeon       = "0"
    var migwanri          = "0"
    var companyCode       = ""    // 조회에서 가져온 회사/창고코드
    var wareHouseCode     = ""

    var companyCodeOut    = "0001"
    var wareHouseCodeOut  = "1001"
    var companyCodeOut0   = "0001"
    var wareHouseCodeOut0 = "1001"
    var CompanySel        = 0
    var WareHouseSel      = 0
    var FirstSetSWOut     = 0    // 출고 사업장코드와 창고코드 처음 한번 적용하기 위한 것
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn     = "0001"
    var wareHouseCodeIn   = "1001"
    var companyCodeIn0    = "0001"
    var wareHouseCodeIn0  = "1001"
    var FirstSetSWIn      = 0    // 입고 사업장코드와 창고코드 처음 한번 적용하기 위한 것
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate           = ""

    // sort의 상태를 파악하기 위한 변수
    var onClickPummokcode     = 0
    var onClickPummyeong      = 0
    var onClickDobeonModel    = 0
    var onClickSayang         = 0
    var onClickLocation       = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail)

        mYocheongbeonho = intent.getStringExtra("yocheongbeonho").toString()
        binding.yocheongbeonho.text = "요청번호 - $mYocheongbeonho"

        LoginUserUtil.getLoginData()?.let {
            sawonCode         = it.sawoncode.toString()
            companyCodeOut0   = it.saeopjangcode.toString()  // by jung 2022.07.02
            wareHouseCodeOut0 = it.changgocode.toString()    // by jung 2022.07.02
            companyCodeIn0    = it.saeopjangcode.toString()  // by jung 2022.07.20
            wareHouseCodeIn0  = it.changgocode.toString()    // by jung 2022.07.20
            wareHouseCodeIn0  = "2002"                       // by jung 2022.07.20 생산자재창고
        }
        binding.chulgodamdangjacode.text = sawonCode

        setupEvents()
        dateSet()
        PostRequestRequest()
        sort()
        spinnerSetOut()
        spinnerSetIn()
        completeTextView()
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            if(status=="333"){
                backDialog() {
                    workStatusCancle()
                    SerialManageUtil.clearData()
                }
            }
            else {
                finish()
            }
        }

        // 검색버튼 클릭시
        binding.btnFind.setOnClickListener {
            if(status=="111"){
                requestWorkseq()
            }
            else if (status == "333"){
                status333Dialog(){
                    SerialManageUtil.clearData()
                    requestWorkseq()

                    FirstSetSWOut = 0   // 사업장코드와 창고코드 처음 한번 적용하기 위한 것  by jung 2022.07.02
                    FirstSetSWIn  = 0   // 사업장코드와 창고코드 처음 한번 적용하기 위한 것  by jung 2022.07.20
                    spinnerSetOut()    // by jung 2022.07.02
                }
            }
        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen   .isVisible = true
            binding.btnFold   .isVisible = false
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen   .isVisible = false
            binding.btnFold   .isVisible = true
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

        binding.btnSave.isVisible = true

    }
    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid"         to "04",
            "tablet_ip"   to IPUtil.getIpAddress(),
            "sawoncode"   to sawonCode,
            "status"      to "111",
        )

        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq

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

        companyCode = intent.getStringExtra("companyCode").toString()
        wareHouseCode = intent.getStringExtra("wareHouseCode").toString()

        Log.d("yj", "조회조건 : $johoejogeon , 미관리 :$migwanri")
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

                        if(it.returnPummokDetail().size == 0){
                            searchZeroDialog()
                            setValues()
                            mAdapter.clearList()
                            status = "111"

                        }else{
                            requestDetailData = it
                            setValues()
                            status = "333"
                        }
                    }
                }
                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<RequestDetailResponse>, t: Throwable) {
                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                loadingDialog.dismiss()
            }
        })
    }

    fun PostRequestRequest() {

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

            requestDetailData.returnPummokDetail().forEach {

                if (it.getPummokCount() == "0" || it.getPummokCount() == null) {  // 출고수량이 0일때는 체크없음
                    return@forEach
                }

                var serialData =
                    SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
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

                val requestChulgodetail = JsonArray()

                requestDetailData.returnPummokDetail().forEach {

                    if(it.getPummokCount() == "0" || it.getPummokCount() == null){
                        return@forEach
                    }

                    val serialData =
                        SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                            .toString()

                    requestChulgodetail.add(
                        RequestChulgodetail(        //check : 요청번호?
                            it.getPummokcodeHP(),
                            it.getPummokCount(),
                            it.getjungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()
                    )
                }

                val requestAdd = hashMapOf(
                    "requesttype"         to "02063",
                    "yocheongbeonho"      to mYocheongbeonho,
                    "chulgoilja"          to calDate,
                    "chulgosaupjangcode"  to companyCodeOut,
                    "chulgochanggocode"   to wareHouseCodeOut,
                    "chulgodamdangjacode" to sawonCode,
                    "ipgosaupjangcode"    to companyCodeIn,
                    "ipgochanggocode"     to wareHouseCodeIn,
                    "ipgodamdangjacode"   to ipgodamdangjacode,
                    "seq"                 to SEQ,
                    "status"              to "777",
                    "pummokcount"         to requestChulgodetail.size().toString(),
                    "chulgodetail"        to requestChulgodetail
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

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

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

            if (FirstSetSWOut == 0) {
                var iCnt = binding.spinnerCompanyOut.count
                for ( i: Int in 0 until iCnt) {
                    if (it.getCompanyCode()[i].code == companyCodeOut0){
                        CompanySel = i
                    }
                }
                binding.spinnerCompanyOut.setSelection(CompanySel)
            }

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

                            if (FirstSetSWOut == 0) {
                                var iCnt = binding.spinnerWareHouseOut.count
                                for ( i: Int in 0 until iCnt) {
                                    if (it.getGwangmyeongCode()[i].code == wareHouseCodeOut0){
                                        WareHouseSel = i
                                    }
                                }
                                binding.spinnerWareHouseOut.setSelection(WareHouseSel,false)

                                if (mWareHouseListOut.size > 0) {
                                    wareHouseCodeOut = mWareHouseListOut[WareHouseSel].code
                                }
                                FirstSetSWOut = 1
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
                            if (FirstSetSWOut == 0) {
                                var iCnt = binding.spinnerWareHouseOut.count
                                for ( i: Int in 0 until iCnt) {
                                    if (it.getGumiCode()[i].code == wareHouseCodeOut0){
                                        WareHouseSel = i
                                    }
                                }
                                binding.spinnerWareHouseOut.setSelection(WareHouseSel,false)

                                if (mWareHouseListOut.size > 0) {
                                    wareHouseCodeOut = mWareHouseListOut[WareHouseSel].code
                                }
                                FirstSetSWOut = 1
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

    // 입고 사업장코드, 창고코드 Event 세팅
    fun spinnerSetIn() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyIn.adapter = spinnerCompanyAdapter

            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseIn.adapter = spinnerWareHouseAdapter

            if (FirstSetSWIn == 0) {
                var iCnt = binding.spinnerCompanyIn.count
                for ( i: Int in 0 until iCnt) {
                    if (it.getCompanyCode()[i].code == companyCodeIn0){
                        CompanySel = i
                    }
                }
                binding.spinnerCompanyIn.setSelection(CompanySel)
            }

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

                            if (FirstSetSWIn == 0) {
                                var iCnt = binding.spinnerWareHouseIn.count
                                for ( i: Int in 0 until iCnt) {
                                    if (it.getGwangmyeongCode()[i].code == wareHouseCodeIn0){
                                        WareHouseSel = i
                                    }
                                }
                                binding.spinnerWareHouseIn.setSelection(WareHouseSel,false)

                                if (mWareHouseListIn.size > 0) {
                                    wareHouseCodeIn = mWareHouseListIn[WareHouseSel].code
                                }
                                FirstSetSWIn = 1
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
                            if (FirstSetSWIn == 0) {
                                var iCnt = binding.spinnerWareHouseIn.count
                                for ( i: Int in 0 until iCnt) {
                                    if (it.getGumiCode()[i].code == wareHouseCodeIn0){
                                        WareHouseSel = i
                                    }
                                }
                                binding.spinnerWareHouseIn.setSelection(WareHouseSel,false)

                                if (mWareHouseListIn.size > 0) {
                                    wareHouseCodeIn = mWareHouseListIn[WareHouseSel].code
                                }
                                FirstSetSWIn = 1
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
                    mAdapter.setList(requestDetailData.returnPummokDetail())
                }
                1 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(requestDetailData.getDownPummokcode())
                }
                2 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropup)
                    mAdapter.setList(requestDetailData.getUpPummokcode())
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
                    mAdapter.setList(requestDetailData.returnPummokDetail())
                }
                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(requestDetailData.getDownPummyeong())
                }
                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(requestDetailData.getUpPummyeong())
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
                    mAdapter.setList(requestDetailData.returnPummokDetail())
                }
                1 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(requestDetailData.getDownDobeonModel())
                }
                2 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropup)
                    mAdapter.setList(requestDetailData.getUpDobeonModel())
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
                    mAdapter.setList(requestDetailData.returnPummokDetail())
                }
                1 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(requestDetailData.getDownSayang())
                }
                2 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropup)
                    mAdapter.setList(requestDetailData.getUpSayang())
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
        val dialog = RequestDetailDialog()

        dialog.setCount(mYocheongbeonho,data, setTempData())
        dialog.show(supportFragmentManager, "dialog_request")
    }

    // 시스템 종료키(태블릿 PC 아랫쪽 세모 버튼)를 누른 경우
    override fun onBackPressed() {
        if(status=="333"){
            backDialog() {
                SerialManageUtil.clearData()
                workStatusCancle() // 작업상태취소를 서버에 통보하고 확인받는 루틴(이안에서 login테이블의 상태정보 update되어야 한다.)
            }
        }
        else {
            finish()
        }
    }

    override fun onDismiss(p0: DialogInterface?) {

        mAdapter.notifyDataSetChanged()
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }

}