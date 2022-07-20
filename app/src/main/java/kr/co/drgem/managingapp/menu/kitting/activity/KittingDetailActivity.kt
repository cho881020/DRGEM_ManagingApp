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

    val loadingDialog     = LoadingDialogFragment()

    var mkittingbeonho    = ""

    var SEQ               = ""
    var status            = "111"

    var sawonData         = ArrayList<SawonData>()
    var sawonCode         = ""
    var ipgodamdangjacode = ""

    var johoejogeon       = "0"
    var migwanri          = "0"
    var changgocode       = ""

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
    var onClickYocheongBeonho = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting_detail)

        mkittingbeonho = intent.getStringExtra("kittingbeonho").toString()
        binding.kittingbeonho.text = "키팅번호 - $mkittingbeonho"

        LoginUserUtil.getLoginData()?.let {
            sawonCode         = it.sawoncode.toString()
            companyCodeOut0   = it.saeopjangcode.toString()  // by jung 2022.07.02
            wareHouseCodeOut0 = it.changgocode.toString()    // by jung 2022.07.02
            companyCodeIn0    = it.saeopjangcode.toString()  // by jung 2022.07.20
            wareHouseCodeIn0  = it.changgocode.toString()    // by jung 2022.07.20
            wareHouseCodeIn0  = "2002"                       // by jung 2022.07.20 생산자재창고
        }
        binding.chulgodamdangjacode.text = sawonCode

        setupEvents()        // Event 설정
        dateSet()            // 일자 선택 박스 Event 설정
        postRequestKitting() // 일괄출고등록 // binding.btnSave.setOnClickListener {  //저장버튼 Event만 별도로 기술한 함수)
        sort()               // sort 기능 Event 설정

        spinnerSetOut()      // 출고 사업장코드, 창고코드 Event 설정
        spinnerSetIn()       // 입고 사업장코드, 창고 코드 Event 설정

        completeTextView()   // 사원명 AutoComplete 기능
    }

    // 시스템 종료키(태블릿 PC 아랫쪽 세모 버튼)를 누른 경우
    override fun onBackPressed() {
        if(status=="333"){
            backDialog() {
                workStatusCancle()            // 작업상태취소를 서버에 통보하고 확인받는 루틴(이안에서 login테이블의 상태정보 update되어야 한다.)
                SerialManageUtil.clearData()  // 시리얼번호 저장된것 clear
            }
        }
        else {
            finish() // 현재의 상세업무을 종료하고 이전으로 돌아감
        }
    }

    override fun setupEvents() {

        // 뒤돌아 가기
        binding.btnBack.setOnClickListener {
            if(status=="333"){
                backDialog() {
                    workStatusCancle()            // 작업상태취소를 서버에 통보하고 확인받는 루틴(이안에서 login테이블의 상태정보 update되어야 한다.)
                    SerialManageUtil.clearData()  // 시리얼번호 저장된것 clear
                }
            }
            else{
                finish()  // 현재의 상세업무을 종료하고 이전으로 돌아감
            }
        }

        // 접기 - 화면의 중간 디스플레이 영역(저장할때 지정되는 항목)을 안보이도록
        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen   .isVisible = true
            binding.btnFold   .isVisible = false
        }

        // 열기 - 화면의 중간 디스플레이 영역(저장할때 지정되는 항목)을 보이도록
        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen   .isVisible = false
            binding.btnFold   .isVisible = true
        }

        // 검색버튼 클릭시
        binding.btnFind.setOnClickListener {
            if(status=="111"){        // 아직 검색된 적이 없는 경우 // FirstSetSWOut = 0  // spinnerSetOut() 는 처음 초기화에서 진행 된다.
                requestWorkseq()      // 작업 SEQ 요청, 상세정보 다운로드, Adapter를 이용하여 화면에 데이터 디스플레이 까지
            }
            else if (status == "333"){
                status333Dialog(){
                    requestWorkseq()  // 작업 SEQ 요청, 상세정보 다운로드, Adapter를 이용하여 화면에 데이터 디스플레이 까지

                    FirstSetSWOut = 0   // 사업장코드와 창고코드 처음 한번 적용하기 위한 것  by jung 2022.07.02
                    FirstSetSWIn  = 0   // 사업장코드와 창고코드 처음 한번 적용하기 위한 것  by jung 2022.07.20
                    spinnerSetOut()   // by jung 2022.07.02
                }
            }
        }

        // 전체 래디오버튼 클릭시
        binding.radio0.setOnClickListener {
            johoejogeon = "0"
        }

        // 미출고 래디오버튼 클릭시
        binding.radio1.setOnClickListener {
            johoejogeon = "1"
        }

        // 미관리항목포함전체 체크박스 체크여부
        binding.checkMigwanri.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                migwanri = "0"
            }else{
                migwanri = "1"
            }
        }
    }

    // 검색후 상세정보 받은 다음 데이터 디스플레이 할 때
    // override fun setValues() { ...
    //    mAdapter.setTemp(setTempData())-------------------------
    // 출고사업장 변경시
    // binding.spinnerWareHouseOut.onItemSelectedListener
    // 출고창고 변경시
    // binding.spinnerCompanyOut.onItemSelectedListener
    // 위의 3번의 경우 setTempData()가 불려진다.
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

    // 서버로부터 받은 상세정보 데이터를 Adapter, recyclerView를 이용하여 디스플레이
    // 검색키를 눌러서 상세정보가 정상적으로 서버로부터 내려온 경우만 불려져서 처리된다.
    override fun setValues() {

        mAdapter = KittingDetailListAdapter(this)          // Adapter 연결
        mAdapter.setTemp(setTempData())                           // Adapter 에 기술되어 있지만 기본 실행이 안되는 문장, 여기서 실행시킨다.
        mAdapter.setList(kittingDetailData.returnKittingDetail()) // Adapter 에 기술되어 있지만 기본 실행이 안되는 문장, 여기서 실행시킨다.
        binding.recyclerView.adapter = mAdapter                   // 이순간에 데이터를 이동시키고 화면에 보여주도록 작업한다.

        binding.txtCount.text = "(${kittingDetailData.getPummokCount()} 건)"

//        kittingDetailData.returnKittingDetail().forEach {
//            if (it.jungyojajeyeobu == "Y") {
//                binding.serialDetail.isVisible = true
//            }
//        }

        binding.btnSave.isVisible = true      // 저장하기 버튼
    }

    // 작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)  // 모래시계 기능 시작

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid"         to "03",
            "tablet_ip"   to IPUtil.getIpAddress(),
            "sawoncode"   to sawonCode,
            "status"      to "111",
        )

        Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")

        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq           // SEQ 저장

                            SerialManageUtil.clearData()  // 시리얼번호 입력정보 clear(초기화)
                            getRequestKittingDetail()     // 키팅명세요청 status 333 세팅하고 받은 상세 데이터 표시

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

    // 키팅명세요청 정상이면 status 333 세팅 데이터 없으면 111 세팅
    // 정상이면 받은 데이터를 kittingDetailData에 저장하고
    // setValues() 함수로 Adapter로 연결시켜 데이터가 디스플레이 되도록한다.
    // ??? 여기서 333 세팅하고 복구시 사용 할 로컬테이블 login 정보에도 333정보를 세팅하여야 한다.
    // 상태정보 변경시는 항상 로컬테이블 login 정보에 업데이트 필요
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
                            kittingDetailData = it  // 받은 정보를 저장
                            setValues()             // 저장된 데이터 표시

                            status = "333"

                            if (it.returnKittingDetail().size == 0) { // 데이터 없으며
                                searchZeroDialog()                    // 데이터 없음 메세지 표시
                                mAdapter.clearList()                  // 상세데이터 clear

                                status = "111"
                            }
                        }
                    }
                    loadingDialog.dismiss()  // 모래시계 기능 정지
                }

                override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                    loadingDialog.dismiss()  // 모래시계 기능 정지
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                }
            })
    }

    // 일괄출고등록 - 저장버튼에 대한 Event 정의 함수
    fun postRequestKitting() {

        // 저장버튼
        binding.btnSave.setOnClickListener {

            // 입고담당자 사원코드 가져오기
            val selecSawon = binding.autoCompleteTextView.text.toString()
            sawonData.forEach {
                if(it.sawonmyeong == selecSawon){
                    ipgodamdangjacode = it.sawoncode
                    Log.d("yj", "사원명 : $selecSawon , 사원코드 : ${it.sawoncode}")
                }
            }

            // 입고담당자 사원코드가 없으면
            if(ipgodamdangjacode == ""){
                ipgodamdangjacodeDialog()                    // 입고담당자 입력요청 메세지 표시
                binding.autoCompleteTextView.requestFocus()  // 입고담당자에 포커스가 가도록 한다. by jung 2022.07.07
                return@setOnClickListener                    // 저장 업무 종료 (binding.btnSave.setOnClickListener 종료)
            }

            kittingDetailData.returnKittingDetail().forEach {

                if (it.getPummokCount() == "0" || it.getPummokCount() == null) {  // 출고수량이 0일때는 체크없음
                    return@forEach
                }

                var serialData =
                    SerialManageUtil.getSerialStringByPummokCode("${it.getPummokcodeHP()}/${it.getyocheongbeonhoHP()}")
                        .toString()      // "품목코드+요청번호"로 시리얼번호 데이터 가져오기

                if (it.jungyojajeyeobu == "Y") {          // 중요자재인 경우
                    val serialSize = serialData.split(",").size  // 시리얼번호의 갯수를 파악

                    if (serialSize.toString() != it.getPummokCount() || serialData == "null") {   // 시리얼번호 입력에 갯수가 안맞으면
                        countSerialDialog()   // 오류메시지 표시
                        it.serialCheck = true
                        mAdapter.notifyDataSetChanged()
                        serialData = ""

                        return@setOnClickListener    // 함수 종료 처리 (btnSave button Event 종료)
                    } else {
                        it.serialCheck = false
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            saveDialog() {   // 여기까지 정상이면 데이터 save 할 것인지 확인 메시지 표시

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
                    "requesttype"         to "02053",
                    "kittingbeonho"       to mkittingbeonho,
                    "chulgoilja"          to calDate,
                    "chulgosaupjangcode"  to companyCodeOut,
                    "chulgochanggocode"   to wareHouseCodeOut,
                    "chulgodamdangjacode" to sawonCode,
                    "ipgosaupjangcode"    to companyCodeIn,
                    "ipgodamdangjacode"   to ipgodamdangjacode,
                    "ipgochanggocode"     to wareHouseCodeIn,
                    "seq"                 to SEQ,
                    "status"              to "777",
                    "pummokcount"         to chulgodetail.size().toString(),
                    "chulgodetail"        to chulgodetail
                )

                Log.d("yj", "일괄출고등록 맵확인 : $kittingAdd")

                if (chulgodetail.size() > 0) {   // 출고 데이터가 준비 되면
                    apiList.postRequestDeliveryBatch(kittingAdd)
                        .enqueue(object : Callback<WorkResponse> {
                            override fun onResponse(
                                call: Call<WorkResponse>,
                                response: Response<WorkResponse>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {      // 정상으로 전송되었으면
                                            status = "111"               // 상태를 '111'로 ---> 이때도 login 정보에 update 되어야 한다.
                                            SerialManageUtil.clearData() // 시리얼번호 데이터 영역 clear - 검색키를 눌러서 상세내역조회가 완료되면 이문장이 실행된다.
                                                                         // 일단 그대로 둔다. 아래의 문장에 영향을 준다???
                                            mAdapter.clearList()         // 상세정보 영역 mAdapter clear
                                            saveDoneDialog()             // 저장(전송) 완료 메시지 표시

                                        } else {
                                            serverErrorDialog(it.resultmsg)  // 오류발생시 서버에서 받은 메시지 표시
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {  // 통신오류 발생시
                                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                            }
                        })
                } else {                 // 출고 데이터가 하나도 없으면
                    saveNotDoneDialog()  // 저장데이터가 없음을 표시
                }
            }
        }
    }

    // 작업상태취소 서버에 통보하고 확인받는 루틴
    fun workStatusCancle() {

        val workCancelMap = hashMapOf(  // 취소 요청 데이터 준비
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
                            // 여기서도 login 테이블에 정보를 update해야 한다.
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

    // 출고 사업장코드, 창고코드 Event 세팅
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
                        Log.d("yj", "wareHouseCodeIn : $wareHouseCodeIn")
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        }
    }

    // 전체 소트 작업
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
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }
                1 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownPummokcode())
                }
                2 -> {
                    binding.imgPummokcode.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpPummokcode())
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
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }
                1 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownDobeonModel())
                }
                2 -> {
                    binding.imgDobeonModel.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpDobeonModel())
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
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }
                1 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownSayang())
                }
                2 -> {
                    binding.imgSayang.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpSayang())
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

        // 요청번호 소트
        binding.layoutYocheongBeonho.setOnClickListener {

            sortImageClear(6)

            if (onClickYocheongBeonho < 2) {
                onClickYocheongBeonho++
            } else {
                onClickYocheongBeonho = 0
            }

            when (onClickYocheongBeonho) {
                0 -> {
                    binding.imgYocheongBeonho.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(kittingDetailData.returnKittingDetail())
                }
                1 -> {
                    binding.imgYocheongBeonho.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(kittingDetailData.getDownYocheongBeonho())
                }
                2 -> {
                    binding.imgYocheongBeonho.setImageResource(R.drawable.dropup)
                    mAdapter.setList(kittingDetailData.getUpYocheongBeonho())
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
        if (iP != 6){
            onClickYocheongBeonho   = 0
            binding.imgYocheongBeonho.setImageResource(R.drawable.dropempty)
        }
    }

    // 일자 선택 박스 Event 설정
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

    // 사원명 AutoComplete 기능
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

    // 상세정보 조회시에서 정보입력 버튼을 클릭하면 시리얼번호 입력화면을 표시한다.
    override fun onClickedEdit(data: Pummokdetail) {
        val dialog = KittingDetailDialog()  //시리얼번호 입력화면
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