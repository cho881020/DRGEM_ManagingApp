/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : StockActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 재고조사 화면으로 품목정보요청 및 재고수량등록 기능
 */

package kr.co.drgem.managingapp.menu.stock.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityStockBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
import kr.co.drgem.managingapp.menu.stock.StockListEditListener
import kr.co.drgem.managingapp.menu.stock.adapter.StockListAdapter
import kr.co.drgem.managingapp.menu.stock.dialog.LoadingStockDialogFragment
import kr.co.drgem.managingapp.menu.stock.dialog.StockDialogFragment
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay as delay

class StockActivity : BaseActivity(), StockListEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityStockBinding
    lateinit var mAdapter: StockListAdapter

    lateinit var productData: ProductInfoResponse
    var inputCode = ""

    var companyCode   = "0002"        // 조회코드
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()

    var saeopjangcode = ""          // 등록코드
    var changgocode   = ""

    val loadingDialog   = LoadingStockDialogFragment()
    val stockCodeDialog = StockDialogFragment()

    var SEQ       = ""
    var status    = "111"
    var sawonCode = ""

    var MakeSeqNum = 1  // 리스트 등록시 순번 만들기 용

    var AddUpdateCheckSw = 0 // 0 은 기본으로 추가 버튼, 1은 정정 버튼
    var SavedSeqNum      = 0  // 순번을 저장

    val mList: ArrayList<Pummokdetail> = arrayListOf()  // 리스트 추가시 화면에 보일 목록
    val mListSeq: ArrayList<PummokdetailStock> = arrayListOf()  // 리스트 추가시 화면에 보일 목록 순번을 추가한 것
    lateinit var searchCodeData: PummokdetailStock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

        setupEvents()
        spinnerSet()  // 사업장, 창고 콤보박스 세팅 (재고조사 준비에서 사용)

        // 지울것 (테스트용)
        //binding.edtCode.setText("e07-000710-00")
    }

    // 시스템 종료키(태블릿 PC 아랫쪽 세모 버튼)를 누른 경우
    override fun onBackPressed() {
        backDialog() {
            workStatusCancle() // 작업상태취소를 서버에 통보하고 확인받는 루틴(이안에서 login테이블의 상태정보 update되어야 한다.)
        }
    }

    override fun setupEvents() {

        // 되돌아가기 버튼
        binding.btnBack.setOnClickListener {
            backDialog() {
                workStatusCancle()
            }
        }

        // 저장하기 버튼 (추가된 모든 데이터 일괄 서버로 전송)
        binding.btnSave.setOnClickListener {
            stockCodeDialog.show(supportFragmentManager, null)
        }

        // 품목코드 지우기 버튼
        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }

        // 재고조사 준비 버튼
        binding.btnReady.setOnClickListener {
            requestWorkseq()
        }

        searchStock()   // 검색버튼의 클릭에 대한  "binding.btnFind.setOnClickListener {" 정의

        // 초기화 버튼
        binding.btnReset.setOnClickListener {

            // 버튼 명 바꾸기 - 정정업무시 버튼명이 바뀌기 때문에
            binding.btnAdd  .text = "+ 추가"  // 수정
            binding.btnReset.text = "초기화"  // 취소

            binding.layoutAdd.isVisible = false
            binding.layoutFind.isVisible = true

            binding.suryang    .setText("0")
            binding.locationAdd.setText("")
            binding.edtCode    .setText("")

            binding.edtCode.requestFocus()

            // keyboard 올리기
            val imm = binding.edtCode.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.edtCode,0)
        }

        // +추가버튼을 클릭하면
        binding.btnAdd.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateServer = SimpleDateFormat("yyyyMMddhhmmss")              // 서버 전달 포맷
            val josasigan0 = dateServer.format(cal.time)

            //searchCodeData.seqnum          = MakeSeqNum.toString()                // 시퀀스 번호-신규일 경우만 작업
            //searchCodeData.pummokcode      = "" // 품목코드  --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            //searchCodeData.pummyeong       = "" // 품명     --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            //searchCodeData.dobeon_model    = "" // 도번/모델 --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            //searchCodeData.sayang          = "" // 사양     --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            //searchCodeData.danwi           = "" // 단위     --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            //searchCodeData.location        = "" // 위치     --> 이 데이터는 검색버튼을 누른 경우 복사된다.
            searchCodeData.hyeonjaegosuryang = binding.suryang.text.toString()      // 현재고수량
            searchCodeData.josasigan = josasigan0                           // 조사시간
            searchCodeData.locationadd = binding.locationAdd.text.toString()  // 입력된 로케이션

            if (searchCodeData.gethyeonjaegosuryangHP() == "") {
                AlertDialog.Builder(mContext)
                    .setMessage("수량을 입력 해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()
                return@setOnClickListener
            }

//            중복허용 불가시 사용
//            mList.forEach {
//                if (it.pummokcode == searchCodeData.pummokcode) {
//
//                    AlertDialog.Builder(mContext)
//                        .setMessage("이미 추가 된 품목입니다.")
//                        .setNegativeButton("확인", null)
//                        .show()
//
//                    binding.layoutAdd.isVisible = false
//                    binding.layoutFind.isVisible = true
//                    return@setOnClickListener
//
//                }
//            }

            if (AddUpdateCheckSw == 0) { // 0 은 기본으로 추가 버튼, 1은 정정 버튼으로 사용을 지정
                // 여기서는 추가 작업
                searchCodeData.seqnum = MakeSeqNum.toString()  // 시퀀스 번호

                mListSeq.addAll(listOf(searchCodeData))

                setValues()

                MakeSeqNum++  // 추가작업후 순번을 +1 한다.
            } else {   //AddUpdateCheckSw == 0 은 기본으로 추가 버튼, 1은 정정 버튼으로 사용을 지정
                // 여기서는 정정 작업
                mListSeq.forEach {

                    if (it.getSeqNumHP().toInt() == SavedSeqNum) {

                        it.hyeonjaegosuryang = searchCodeData.hyeonjaegosuryang  // 입력된 수량
                        it.locationadd = searchCodeData.locationadd        // 입력된 로케이션
                        it.josasigan = searchCodeData.josasigan          // 입력 작업시간

                        return@forEach
                    }
                }
                // 위에서 일치하는 데이터가 없으면 오류가 발생되어야한다. 로직상은 불필요 - 반드시 일치 데이터 있어야 한다.
                binding.recyclerView.adapter = mAdapter

                // 버튼 명 바꾸기
                binding.btnAdd.text = "+ 추가"  // 수정
                binding.btnReset.text = "초기화"  // 취소
            }

            Log.d("yj", "mList : $mList")

            AddUpdateCheckSw = 0 // 0 은 기본으로 추가 버튼, 1은 정정 버튼으로 사용을 지정

            binding.layoutAdd.isVisible = false
            binding.layoutFind.isVisible = true

            binding.suryang.setText("0")
            binding.locationAdd.setText("")
            binding.edtCode.setText("")

            // 지울것 (테스트용)
            //binding.edtCode.setText("e07-000710-00")

            binding.edtCode.requestFocus()

            // 키보드 올리기
            // 이곳에 있어야 한다.
            var imm = binding.edtCode.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.edtCode, 0)
        }

        // 품목코드 입력 항목
        binding.edtCode.setOnEditorActionListener { textView, actionId, keyEvent ->

            // 영문자 대문자로 변경하기
            val UpperCaseS = binding.edtCode.text.toString().uppercase()
            binding.edtCode.setText(UpperCaseS)

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.edtCode.onEditorAction(5)
                    binding.btnFind.callOnClick()
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener actionId != 5
        }

        // 수량입력항목
        binding.suryang.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.suryang.onEditorAction(5)
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener actionId != 5
        }

        // 로케이션 입력 항목
        binding.locationAdd.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.locationAdd.onEditorAction(5)
                    binding.btnAdd.callOnClick()
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener actionId != 5
        }
    }

    override fun setValues() {

//      mAdapter = StockListAdapter(mList) // 이문장을 아래의 2개로 보완 listener 사용을 위해서
        mAdapter = StockListAdapter(this)

        mAdapter.setList(mListSeq)

        binding.recyclerView.adapter = mAdapter

        // 여기서 focus가 최종 행에 가도록 해야한다.
        val LastPosition = mListSeq.size - 1
        onItemViewClicked(LastPosition)  // 이것이 마지막 추가된 행이 선택된 것처럼 마지막 행을 선택표시한다.
        binding.recyclerView.scrollToPosition(LastPosition)  // 마지막 추가된 행이 보여지도록 한다.
    }

    // 사업장, 창고 콤보박스 등록
    fun spinnerSet() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompany.adapter = spinnerCompanyAdapter

            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

            binding.spinnerCompany.setSelection(1)

            binding.spinnerCompany.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCode = "0001"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouse.setSelection(0, false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }
                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCode = "0002"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGumiCode())
                            binding.spinnerWareHouse.setSelection(0, false)

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
    }

    // 서버에 작업 SEQ 요청 -> 계속해서 서버에 품목정보요청까지
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "07",
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

                            getRequestStock()

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

    // 서버에 품목정보요청
    fun getRequestStock() {

        apiList.getRequestProductinfo("02091", "", companyCode, wareHouseCode)
            .enqueue(object : Callback<ProductInfoResponse> {
                override fun onResponse(
                    call: Call<ProductInfoResponse>,
                    response: Response<ProductInfoResponse>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let {

                            if (it.returnPummokDetail().size == 0) {  //여기서 데이터 받은것을 가져온다.
                                searchZeroDialog()
                                status = "111"

                            } else {
                                status = "333"

                                productData = it

                                val sSize = productData.pummokdetail?.size.toString()

                                loadingDialog.loadingEnd(sSize)

                                binding.layoutEmpty.isVisible = false
                                binding.layoutReady.isVisible = false
                                binding.layoutAdd  .isVisible = false
                                binding.layoutFind .isVisible = true
                                binding.layoutList .isVisible = true
                                binding.btnSave    .isVisible = true
                                binding.edtCode    .requestFocus()

                                // SHOW_IMPLICIT 가 지정되어야 종료시 키보드가 사라진다.
                                // deprecated된 toggleSoftInput() 함수를 사용해야만 키보드가 올라온다.
                                // showSoftInput() 함수를 사용하면 먹히지 않는다. 통신시 쓰레드의 연관???
                                var imm = binding.edtCode.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ProductInfoResponse>, t: Throwable) {
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                }
            })
    }

    // 재고수량등록 - 서버로 전송 작업
    fun postRequestStock() {

        val stockAddList = JsonArray()

        mListSeq.forEach {
            var pummokcode = ""
            var suryang    = ""

            pummokcode = it.getPummokcodeHP()
            suryang    = it.gethyeonjaegosuryangHP()

            stockAddList.add(
                StockPummokdetail(
                    pummokcode,
                    suryang,
                    it.getjosasiganHP(),
                    it.getLocationaddHP()
                ).toJsonObject()
            )
        }

        val stockAdd = hashMapOf(
            "requesttype" to "02092",
            "seq" to SEQ,
            "tabletip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "saeopjangcode" to saeopjangcode,
            "changgocode" to changgocode,
            "status" to "777",
            "pummokcount" to mListSeq.size.toString(),
            "pummokdetail" to stockAddList
        )

        Log.d("yj", "재고등록맵확인 : $stockAdd")

        apiList.postRequestStock(stockAdd).enqueue(object : Callback<WorkResponse> {
            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.resultcd == "000") {

                            saveDoneDialog()

                            mListSeq.clear()
                            setValues()

                            MakeSeqNum = 1  // 리스트 등록시 순번 만들기 용

                            binding.layoutEmpty.isVisible = true
                            binding.layoutReady.isVisible = true
                            binding.layoutAdd  .isVisible = false
                            binding.layoutFind .isVisible = false
                            binding.layoutList .isVisible = false
                            binding.btnSave    .isVisible = false
                            binding.suryang    .setText("0")
                            binding.locationAdd.setText("")
                        } else {
                            serverErrorDialog(it.resultmsg)
                        }
                        Log.d("yj", "재고등록 콜 결과코드 : ${it.resultcd}")
                        Log.d("yj", "재고등록 콜 결과메시지 : ${it.resultmsg}")
                    }
                }
            }

            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
            }
        })
    }

    // 작업상태취소
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

    fun searchStock() {

        // 검색버튼을 클릭
        // 다운로드된 데이터에서 해당 품목코드의 데이터를 복사한다.
        binding.btnFind.setOnClickListener {

            // 영문자 대문자로 변경하기
            val UpperCaseS = binding.edtCode.text.toString().uppercase()
            binding.edtCode.setText(UpperCaseS)

            inputCode = binding.edtCode.text.toString()

            //searchCodeData = Pummokdetail("", "", "", "", "", "", "", "", "", "", "", "")
            searchCodeData = PummokdetailStock("", "", "", "", "", "", "", "", "","")

            productData.returnPummokDetail().forEach {

                if (inputCode == it.pummokcode) {    // it.pummokcode는 받아온 전체 데이터의 품목코드
                    // searchCodeData = it 이전에 productData와 searchCodeData의 형식이 같았을 경우는 통째로 넘긴다
                    // 하지만 형식이 바뀌어서 searchCodeData는 원하는 데이터만을 편집하여 사용하는 방식으로 변경됨, 위의 문장은 오류가 됨

                    searchCodeData.seqnum            = ""                    // 시퀀스 번호//+추가 버튼을 클릭하면 만든다.
                    searchCodeData.pummokcode        = inputCode             // 품목코드
                    searchCodeData.pummyeong         = it.pummyeong          // 품명
                    searchCodeData.dobeon_model      = it.dobeon_model       // 도번/모델
                    searchCodeData.sayang            = it.sayang             // 사양
                    searchCodeData.danwi             = it.danwi              // 단위
                    searchCodeData.location          = it.location           // 위치
                    searchCodeData.locationadd       = ""                    // 입력된 로케이션//+추가 버튼을 클릭하면 만든다.
                    searchCodeData.hyeonjaegosuryang = it.hyeonjaegosuryang  // 현재고수량
                    searchCodeData.josasigan         = ""                    // 조사시간//+추가 버튼을 클릭하면 만든다.

                    // 화면에 보여주는 데이터
                    binding.pummokcode .text  = inputCode
                    binding.pummyeong  .text  = searchCodeData.getpummyeongHP()
                    binding.dobeonModel.text  = searchCodeData.getdobeon_modelHP()
                    binding.sayang     .text  = searchCodeData.getsayangHP()
                    binding.suryang    .setText(searchCodeData.gethyeonjaegosuryangHP())
                    binding.locationAdd.setText(searchCodeData.getlocationHP())
                }
            }

            if (searchCodeData.pummokcode == "") {
                searchZeroDialog()
                binding.edtCode.requestFocus()
                binding.edtCode.selectAll()
                return@setOnClickListener
            }

            binding.layoutAdd.isVisible = true
            binding.layoutFind.isVisible = false

            binding.suryang.requestFocus()
        }
    }

//    // 상세정보 조회시에서 정보입력 버튼을 클릭하면 시리얼번호 입력화면을 표시한다.
//    override fun onClickedEdit(data: Pummokdetail) {
//        val dialog = KittingDetailDialog()  //시리얼번호 입력화면
//        dialog.setCount(mkittingbeonho,data, setTempData())
//        dialog.show(supportFragmentManager, "Kitting_dialog")
//    }

    // 상세정보 조회시에서 수량 사각형 버튼을 클릭하면 수량 정정으로 화면을 바꾼다.
    override fun onClickedEdit(data: PummokdetailStock) {

        AddUpdateCheckSw = 1 // 0 은 기본으로 추가 버튼, 1 은 정정 버튼으로 사용을 지정

        // 아래의 문장 1개로 인해서 현재의 물려있는 데이터의 위치가 결정되는 것 같다.??????
        // 이것이 없을 때는 수정하는 내용이 항상 검색에서 처리된 마지막 추가항목에 반영되어 2개가 수정이 동시에 된다.
        // 검색이 완료되면 이 문장이 사용된다.
        // 이것을 여기에 추가하는 데, 하루의 2/3 정도 걸린 것 같다.
        searchCodeData = PummokdetailStock("", "", "", "", "", "", "", "", "","")

        searchCodeData.seqnum            = data.seqnum             // 시퀀스 번호//+추가 버튼을 클릭하면 만든다.
        searchCodeData.pummokcode        = data.pummokcode         // 품목코드
        searchCodeData.pummyeong         = data.pummyeong          // 품명
        searchCodeData.dobeon_model      = data.dobeon_model       // 도번/모델
        searchCodeData.sayang            = data.sayang             // 사양
        searchCodeData.danwi             = data.danwi              // 단위
        searchCodeData.location          = data.location           // 위치
        searchCodeData.locationadd       = data.locationadd        // 입력된 로케이션
        searchCodeData.hyeonjaegosuryang = data.hyeonjaegosuryang  // 현재고수량
        searchCodeData.josasigan         = data.josasigan          // 조사시간//+추가 버튼을 클릭하면 만든다.

        // 화면에 보여주기 위한 데이터 이동
        binding.pummokcode .setText(data.pummokcode)          // 품목코드
        binding.pummyeong  .setText(data.pummyeong)           // 품명
        binding.suryang    .setText(data.hyeonjaegosuryang)   // 수량
        binding.dobeonModel.setText(data.dobeon_model)        // 도번
        binding.sayang     .setText(data.sayang)              // 사양
        binding.locationAdd.setText(data.locationadd)         // 로케이션

        // 정정시 순번으로 해당데이터를 찾기 위함
        SavedSeqNum = data.getSeqNumHP().toInt()

        // 버튼 명 바꾸기
        binding.btnAdd  .text = "수정"  // + 추가
        binding.btnReset.text = "취소"  // 초기화

        binding.layoutAdd .isVisible = true   // 상세정보 디스플레이하고 추가, 초기화 하는 영역
        binding.layoutFind.isVisible = false  // 품목코드 입력하고 검색하는 영역

        binding.suryang.requestFocus()

        // keyboard 올리기
        val imm = binding.suryang.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.suryang,0)
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }

    override fun onDismiss(p0: DialogInterface?) {

        saeopjangcode = stockCodeDialog.companyCode
        changgocode   = stockCodeDialog.wareHouseCode
        Log.d("yj", "saeopjangcode : $saeopjangcode , changgocode : $changgocode")

        saveDialog() {
            postRequestStock()
        }
    }
}
