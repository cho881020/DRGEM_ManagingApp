/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingDetailDialog
 * 개 발 자 :  (주)디알젬
 * 업무기능 : 키팅출고업무 시리얼번호 입력루틴
 */
package kr.co.drgem.managingapp.menu.kitting.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.BaseDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogKittingDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.menu.order.dialog.DialogEditOrderAdapter
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KittingDetailDialog : BaseDialogFragment() {

    lateinit var binding: DialogKittingDetailBinding
    lateinit var mAdapter: DialogEditKittingAdapter

    val mSerialDataList  = ArrayList<SerialLocalDB>()
    val mSerialDataListC = ArrayList<SerialLocalDB>()

    var viewholderCount  = 0
    lateinit var pummokData: Pummokdetail
    var mKittingbeonho   = ""
    lateinit var tempData: TempData
    var beforeSuryang   = 0   // 작업전에 가지고 있던 수량, 이전에 수량이 0이 아니였는데 0으로 바뀐경우 전송하기 위해

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_kitting_detail, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 시스템의 종료버튼을 누른 경우 검사하기 루틴  by jung 2022.07.09
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                //TODO: back key 이벤트 시 필요한 코드 추가
                onBackPressed()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        setValues()
        setupEvents()
    }

    fun onBackPressed() {  // by jung 2022.07.09
        binding.btnCancel.callOnClick()
    }

    override fun setupEvents() {

        // 품목코드 clear 버튼
        binding.btnCodeRemove.setOnClickListener {
            binding.edtPummokcode.text = null
        }

        // 등록하기 버튼을 클릭하면  // 루틴 전체를 수정함 by jung 20220711
        binding.btnAdd.setOnClickListener {

            // sw01이 1이면 품목코드만 검사, 2이면 수치만 검사, 3이면 둘다 검사
            // sw02가 1이면 포커스지정, 0이면 포커스 지정 없음
            if (!InputCountCheckChange(3,1)) {  //오류 검사    // by jung 20220701
                return@setOnClickListener
            }

            var inputCount = binding.edtCount.text.trim().toString()

            var errSw = 0  // 서버와 통신시 에러 발생여부 검사용

            // 이전에 수량데이터가 존재하였는데 현재 0인 경우 처리 추가할 것---- start
            // 0으로 업데이트 되도록 정상 작업 진행시킨다.
            if ( 0 == inputCount.toInt() && beforeSuryang == 0 ) {
                AlertDialog.Builder(requireContext())
                    .setMessage("현재의 수량이 0이고 이전에도 0인 경우의 오류입니다.")
                    .setNegativeButton("확인", null)
                    .show()

                return@setOnClickListener
            }

            // 중요자재 항목일 경우만 진행--------------------------------- start
            if (pummokData.getjungyojajeyeobuHP() == "Y") {
                // 시리얼번호 편집
                val contentString = StringBuilder()     // String 문자열 만들기

                for (data in mSerialDataList) {         // 시리얼데이터 목록을 돌기 (data 변수 명으로)
                                                        // mSerialDataList에는 시리얼번호가 개별로 보관되어 있다.
                    if (data.serial.isNotBlank()) {     // data 의 시리얼이 빈값이 아닐 때
                        contentString.append("${data.serial},")        // contentString 에 시리얼을 담기
                    }
                }

                if (contentString.isNotBlank()) {                      // contentString 이 빈 값이 아닐 때
                    contentString.setLength(contentString.length - 1)  // contentString 길이를 1개 줄임 (, 때문에 빈 값을 제외)
                                                                       // 이문장에의해서 제일 끝의 ","가 제거된다.
                } else
                {   // contentString 이 빈 값일 때
                    contentString.setLength(0)  // contentString 길이를 0개로
                }

                // SerialManageUtil 에 값을 담기 (hashMap 형태로) (한줄로 저장)
                // 수량이 0이 아닌 경우는 수량만큼의 시리얼번호가 저장되지만
                // 수량이 0인 경우 해당품목의 시리얼번호는 clear 된다.
                SerialManageUtil.putSerialStringByPummokCode(
                    "${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}",
                    contentString.toString()
                )
                val serialData =  // 위에서 저장한 데이터를 다시 가져온다.
                    SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                        .toString()
                // serialData와 contentString.toString()은 같은 데이터, 다시 읽을 필요는 없었다. (실제 확인함 by jung 2022.07.08)

                Log.d("yj 품목코드 = ", pummokData.getPummokcodeHP())
                Log.d("yj 저장하는 씨리얼스트링 = ", contentString.toString())
                Log.d("yj serialData = ", serialData)

                try {
                    if(inputCount == "0"){
                        //SerialManageUtil.clearData()   // 전체를 다 지우는 것 아닌지 검토 필요 - 해당 데이터만 삭제해야 한다.
                                                       // 위에서 이미 해당 데이터는 0인 경우 clear처리 되었다.
                                                       // 또는 삭제할 필요가 없을수도 있다. 이전 데이터를 보관하려면, 숫자만 가지고 처리한다.
                                                       // 정상 처리시는 숫자보다 시리얼번호의 갯수가 작은 경우만 검사하여 오류처리하고
                                                       // 갯수가 같은 경우만 정상처리 한다.
                                                       // 0인 경우 시리얼번호는 살려둔다.?? - 전체저장시는 0인 경우는 무조건 보내지 않는다.
                    }
                    else if(inputCount.toInt() != serialData.split(",").size){

                        AlertDialog.Builder(requireContext())
                            .setMessage("입력 수량과 시리얼번호 수량이 일치하지 않습니다..")
                            .setNegativeButton("확인", null)
                            .show()

                        //SerialManageUtil.clearData()  // 이놈도 절대 여기있으면 안된다. 모두클리어 된다.
                                                        // 이 문장은 상세리스트(KittingDetailActivity.kt) 페이지에서도 검토되어야한다.
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("수량을 입력해 주세요.")
                        .setNegativeButton("확인", null)
                        .show()

                    return@setOnClickListener
                }
            }
            // 중요자재 항목일 경우만 진행--------------------------------- end

            // 서버로 현재고 임시등록 처리--------------------------------- start
            // 중요자재와 일반자재와 공통 처리
            val tempMap = hashMapOf(
                "requesttype" to "08003",
                "saeopjangcode" to tempData.saeopjangcode,
                "changgocode" to tempData.changgocode,
                "pummokcode" to pummokData.getPummokcodeHP(),
                "suryang" to inputCount,
                "yocheongbeonho" to pummokData.getyocheongbeonhoHP(),
                "ipchulgubun" to "2",
                "seq" to tempData.seq,
                "tablet_ip" to IPUtil.getIpAddress(),
                "sawoncode" to tempData.sawoncode,
                "status" to "333",
            )
            Log.d("yj", "tempMap : $tempMap")

            apiList.postRequestTempExtantstock(tempMap).enqueue(object :
                Callback<WorkResponse> {
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
                ) {
                    // 여기서 login 테이블에 상태정보를 업데이트 해야한다.(333)
                    Log.d("yj", "현재고임시등록 code : ${response.body()?.resultcd}")
                    Log.d("yj", "현재고임시등록 msg : ${response.body()?.resultmsg}")
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "현재고임시등록 서버와 작업시 오류 발생 ")

                    errSw = 1  // 에러발생을 표시하고 되돌리기 위한 처리
                    // 여기서 return@setOnClickListener 문장이 오류가 나기 때문에 errSw 사용
                }
            })
            // 서버로 현재고 임시등록 처리--------------------------------- end

            // 서버전송시 에러가 발생한 경우------------------------------- start
            if (errSw == 1) {   // 여기서 메시지를 보여주고 되돌아간다.
                AlertDialog.Builder(requireContext())
                    .setMessage("현재고임시등록 서버와 작업시 오류 발생.")
                    .setNegativeButton("확인", null)
                    .show()

                return@setOnClickListener
            }
            // 서버전송시 에러가 발생한 경우------------------------------- end

            pummokData.setPummokCount(inputCount)  // 입력 수량을 보관한다.
            saveDoneDialog()
            dismiss()
        }

        // 취소 버튼을 누르면
        binding.btnCancel.setOnClickListener {
            // 품목코드에 입력이 되지 않은 상태이면 묻지않고 돌아간다. 스페이스만 입력된 경우 포함
            if ( binding.edtPummokcode.text == null || binding.edtPummokcode.text.trim().toString() == "") {
                dismiss()
            } else
            {
                AlertDialog.Builder(requireContext())
                    .setTitle("아직 저장하지 않은 사항이 있습니다.")
                    .setMessage("그래도 이 화면을 종료하시겠습니까?")
                    .setNeutralButton("예", DialogInterface.OnClickListener { dialog, which ->
                        dismiss()
                    })
                    .setNegativeButton("아니오", null)
                    .show()
            }
        }

        // 확인 버튼을 누르면 - 시리얼번호을 입력할 수 있는 화면을 전개
        binding.btnOk.setOnClickListener {

            if (InputCountCheckChange(3,1)) {  //오류가 없으면    // by jung 20220701

                val inputCount = binding.edtCount.text.trim().toString()

                try {
                    viewholderCount = inputCount.toInt()
                } catch (e: Exception) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("수량을 입력해 주세요.")
                        .setNegativeButton("확인", null)
                        .show()

                    return@setOnClickListener
                }
                if (pummokData.jungyojajeyeobu == "Y") {
                    adapterSet()

                    // focus 시리얼번호 입력항목으로 가기
                    binding.edtCount.onEditorAction(5) // by jung 20220701
                }
            }
        }

        // 품목코드를 입력하면
        binding.edtPummokcode.setOnEditorActionListener { textView, actionId, keyEvent ->

            // 영문자 대문자로 변경하기
            val UpperCaseS = binding.edtPummokcode.text.toString().uppercase()
            binding.edtPummokcode.setText(UpperCaseS)

            val inputPummokCode = binding.edtPummokcode.text.toString()

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    //binding.edtPummokcode.onEditorAction(5) // by jung 20220701  막음  //이중 에러메세지 발생의 원인??

                    if (pummokData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        if (pummokData.getjungyojajeyeobuHP() == "Y") {
                            binding.btnOk.isVisible = true
                        }
                        binding.btnAdd       .isVisible = true    // 2022.07.12
                        binding.edtPummokcode.isEnabled = false   // 2022.07.12
                        binding.btnCodeRemove.isEnabled = false   // 2022.07.12
                        binding.layoutCount  .isVisible = true
                        binding.edtCount     .requestFocus()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setMessage("품목코드가 일치하지 않습니다.11")
                            .setNegativeButton("확인", null)
                            .show()

                        binding.edtPummokcode.selectAll()    // by jung 20220701
                        binding.edtPummokcode.requestFocus()
                    }
                    return@setOnEditorActionListener true
                }
            }

            // 수작업 입력하고 탭키를 이용하여 진행시     // by jung 20220701---------------------s
            else
            if (actionId == 5) {
                    if (pummokData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        if (pummokData.getjungyojajeyeobuHP() == "Y") {
                            binding.btnOk.isVisible = true
                        }
                        binding.btnAdd       .isVisible = true    // 2022.07.12
                        binding.edtPummokcode.isEnabled = false   // 2022.07.12
                        binding.btnCodeRemove.isEnabled = false   // 2022.07.12
                        binding.layoutCount  .isVisible = true
                        binding.edtCount     .requestFocus()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setMessage("품목코드가 일치하지 않습니다.")
                            .setNegativeButton("확인", null)
                            .show()

                        binding.edtPummokcode.selectAll()
                        binding.edtPummokcode.requestFocus()
                    }
                    return@setOnEditorActionListener true
            }
            // 수작업 입력하고 탭키를 이용하여 진행시     // by jung 20220701---------------------e

            return@setOnEditorActionListener actionId != 5
        }

        binding.edtCount.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {

                if (InputCountCheckChange(3,1)) { // 오류가 없으면// by jung 2022.07.01

                    if (keyEvent.action == KeyEvent.ACTION_UP) {

                        // 일단 수치 입력에서는 제자리에 있도록 한다.   // by jung 2022.07.01
                        // 후에 확인 키를 누르면 다음작업으로 가도록 한다.
                        binding.edtCount.selectAll()
                        binding.edtCount.requestFocus()

                        binding.edtCount.onEditorAction(5)  // by jung 2022.07.01 원본을 막기만 한것
                        binding.btnOk.callOnClick()
                        return@setOnEditorActionListener true
                    }
                }else
                {
                    return@setOnEditorActionListener false
                }
            }

            return@setOnEditorActionListener actionId != 5
        }

//        // 품목코드입력에 포커스가 있다가 다른 곳을 클릭하여 포커스를 잃는 경우 전체 오류 검사 by jung 2022.07.03
//        binding.edtPummokcode.setOnFocusChangeListener { view, b ->
//            //Variable 'b' represent whether this view has focus.
//            //If b is true, that means "This view is having focus"
//            if (!b) {
//                if (InputCountCheckChange(1,0)){ //품목코드만 조사한다.
//                    //binding.btnOk.performClick() //작동은 되지만 포커스가 두개로 나누어지고 이상해 짐
//                }
//            }
//        }
        // 수치입력에 포커스가 있다가 다른 곳을 클릭하여 포커스를 잃는 경우 전체 오류 검사 by jung 2022.07.03
        binding.edtCount.setOnFocusChangeListener { view, b ->
            //Variable 'b' represent whether this view has focus.
            //If b is true, that means "This view is having focus"
            if (!b) {
                binding.edtCount.clearFocus()
                if (InputCountCheckChange(2,0)){  // 수치데이터만 조사한다.
                    binding.btnOk.performClick()  // 오류가 없으면 확인버튼 클릭으로
                } else
                {   // 오류가 발생하면 수치입력으로 돌려보낸다.
                    //view.findFocus().clearFocus()  // 이문장이 실행되면 프로그램 다운됨
                    // 기존에 이동하려는 곳의 포커스를 해제하는 방법을 찾아야 한다.
                    // 그렇지 않으면 아래의 문장을 실행시 포커스가 2개가 되고 프로그램이 이상해 진다.
                    //binding.edtCount.requestFocus()
                    //view.clearFocus() /// 이문장이 실행되면 프로그램 다운되지는 않는데 역시 두개의 포커스가 생김
                    //binding.edtCount.requestFocus()
                }
            }
        }
    }

    override fun setValues() {

        mSerialDataListC.clear() //이것은 한번만 클리어 되도록 한다.  by jung 2022.07.07
        // 작업전에 가지고 있던 수량, 이전에 수량이 0이 아니였는데 0으로 바뀐경우 전송하기 위해
        beforeSuryang = pummokData.getPummokCount().toInt()
        // Pummokdetail.kt의 PummokCount 의 값은 시리얼번호 입력화면에서 등록하기 버튼에의해 등록될때
        // 작업완료 제일 끝에 현재 화면상에 입력된 값이 저장된 것이다.

        binding.kittingbeonho.text = mKittingbeonho
        binding.pummokcode.text = pummokData.getPummokcodeHP()
        binding.pummyeong.text = pummokData.getpummyeongHP()
        binding.dobeonModel.text = pummokData.getdobeon_modelHP()
        binding.sayang.text = pummokData.getsayangHP()
        binding.danwi.text = pummokData.getdanwiHP()
        binding.location.text = pummokData.getlocationHP()
        binding.hyeonjaegosuryang.text = pummokData.gethyeonjaegosuryangHP()
        binding.yocheongsuryang.text = pummokData.getyocheongsuryangHP()
        binding.gichulgosuryang.text = pummokData.getgichulgosuryangHP()
        binding.chulgosuryang.text = viewholderCount.toString()
        binding.jungyojajeyeobu.text = pummokData.getjungyojajeyeobuHP()
        if (pummokData.getjungyojajeyeobuHP() == "Y") {
            binding.layoutSerial.isVisible = true
        }

        binding.edtPummokcode.setText("")
        binding.edtCount.setText("")

        if (pummokData.getPummokCount() != "0") { // 품목 수량이 0이 아니면 처리하는 것 모두 상세 분석할 것 2022.07.08 by jung
                                                  // 여기서 사용되는 품목 수량은 등록하기가 정상으로 작업되면 제일 마지막에 다음의 문장을 실행한다.
                                                  // pummokData.setPummokCount(inputCount)  // 입력 수량을 보관한다.
            Log.d(
                "yj",
                "data.edtPummokCode ${pummokData.getPummokcodeHP()} :pummokCount : ${pummokData.getPummokCount()} edtPummokCode ${binding.edtPummokcode}"
            )
            binding.edtCount.setText(pummokData.getPummokCount())                 // 보관되어있던 수량을 표시한다.
            binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)      // 품목코드의 입력영역을 gray로 만든다.
            binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080)) // 품목코드의 text를 808080의 컬러로 만든다.
            binding.layoutCount.isVisible = true            // 수량 text와 수량입력(edtCount) 칸이 보이도록한다.
            if (pummokData.getjungyojajeyeobuHP() == "Y") { // 중요자재인 경우
                binding.btnOk.isVisible = true              // 확인버튼이 보이고,
                adapterSet()                                // 시리얼번호 입력난을 표시.
            }
        } else
        {
            binding.edtCount.setText(binding.yocheongsuryang.text)  // 수량이 0인 경우 요청수량을 적용시킨다. // 2022.07.15 자재부 요청사항
        }

        Log.d("yj", "binding.edtPummokcode : ${binding.edtPummokcode.text}")
        Log.d("yj", "data.pummokCount : ${pummokData.getPummokCount()}")
    }

    // 시리얼번호 입력 항목들을 표시한다.
    // 입력하러 들어온 처음 실행(override fun setValues() {)에서 한번 불려지고
    // 시리얼번호 입력을 위한 "확인"버튼이 눌려질때마다 불려진다.
    fun adapterSet(){

        val serialData =
            SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()

        mSerialDataList.clear()

        for (i in 0 until viewholderCount) {             // 리스트를 뷰 홀더 갯수만큼 만들어서 어댑터로 보내주기

            var serial = ""

            if (serialList.isNotEmpty() && serialList.size > i) {      // 시리얼리스트가 사이즈 i보다 크거나 같을 때

                serial = serialList[i]
            }

            mSerialDataList.add(
                SerialLocalDB(
                    pummokData.pummokcode!!,
                    serial,
                    "${i}"
                )
            )
        }

        Log.d("yj", "serialList = $serialList")

        mAdapter = DialogEditKittingAdapter(viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter
    }

    fun adapterSet00(){

        val serialData =
            SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()

        //mSerialDataList.clear()  // 아래의 문장으로 대체
        if (serialList.isNotEmpty()){
            mSerialDataList.clear() // parnet부터 넘어온 데이터가 있을 때, 현재 임시로 작업중인 시리얼번호 정장소 클리어
        } else
        {
            //mSerialDataList를 전체 복사한다.
            //mSerialDataListC.clear() 이것은 한번만 클리어 되도록 한다.

            var serialC = ""
            var pCnt = -1

            // mSerialDataList의 데이터를 mSerialDataListC로 복사한다. 이수 mSerialDataList clear
            for (i in 0 until mSerialDataList.size){

                serialC = mSerialDataList[i].serial

                // 이것은 순차적으로 들어가서 빈것이 먼저 차지하고 계속 뒤에 달려서 처음에 빈것들이 차지하고 있는 듯
                // 1번은 1번방에 2번은 2번방에 들어갈 수 있는 방법을 연구해야한다.
                // mSerialDataListC에 i 값(항상 0에서 갰수만큼만 반복)이 존재하면 set(값 변경), 없으면 add처리 한다.

                pCnt = -1
                if((mSerialDataListC.size >0) && (i < mSerialDataListC.size)) {
                    pCnt = mSerialDataListC[i].position.toInt()
                }

                if ( pCnt == i ) {  // 기존에 존재하던 순번의 방
                    mSerialDataListC.set(i,
                        SerialLocalDB(
                            pummokData.pummokcode!!,
                            serialC,
                            "${i}"
                        )
                    )
                } else
                {                  // 신규로 추가되는 순번의 방
                    mSerialDataListC.add(
                        SerialLocalDB(
                            pummokData.pummokcode!!,
                            serialC,
                            "${i}"
                        )
                    )
                }
            }

            mSerialDataList.clear()
        }
        // parent부터 넘어온 데이터가 없을 때, 즉 신규 입력일 때 현재 임시로 작업중인 시리얼번호 저장소 클리어 하지 않는다.
        // 이것은 현재 진행중인 영역의 클리어만 막을 뿐이고, 다음 문장에서 기존 넘어온 데이터가 있으면 그것만큼은 다시 넘어온 데이터가 채워지고
        // 작업중인 데이터의 수가 넘어온 수보다 많았다면 많은 수치만큼은 없어진다.
        // 후에 현장 작업자가 결정할 사항 ???

        for (i in 0 until viewholderCount) {             // 리스트를 뷰 홀더 갯수만큼 만들어서 어댑터로 보내주기

            var serial = ""

            if ((serialList.isEmpty()) && mSerialDataListC.isNotEmpty() && mSerialDataListC.size > i) {
                serial = mSerialDataListC[i].serial
            }
            if (serialList.isNotEmpty() && serialList.size > i) {      // 시리얼리스트가 사이즈 i보다 크거나 같을 때
                serial = serialList[i]
            }

            mSerialDataList.add(
                SerialLocalDB(
                    pummokData.pummokcode!!,
                    serial,
                    "${i}"
                )
            )
        }

        Log.d("yj", "serialList = $serialList")

        mAdapter = DialogEditKittingAdapter(viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter
    }

    // 상세정보 조회시에서 정보입력 버튼을 클릭하면 시리얼번호 입력화면을 표시하기 위해 불려지는
    // KittingDetailActivity.kt의 override fun onClickedEdit(data: Pummokdetail) { 함수에서
    // 호출된다.
    fun setCount(kittingbeonho: String, data: Pummokdetail, tempData: TempData) {
        mKittingbeonho = kittingbeonho
        pummokData = data
        this.tempData = tempData
    }

    // 입력된 품목코드와 수량을 검사하여 오류 제거  // by jung 2022.07.01
    // sw01이 1이면 품목코드만 검사, 2이면 수치만 검사, 3이면 둘다 검사
    // sw02가 1이면 포커스지정, 0이면 포커스 지정 없음
    fun InputCountCheckChange(sw01 : Int,sw02 : Int): Boolean {

        if (( sw01 == 1 ) || ( sw01 == 3 )) {
            // 품목코드가 같은 지 검사
            val inputPummokCode = binding.edtPummokcode.text.toString()
            if (pummokData.getPummokcodeHP() != inputPummokCode) {
                AlertDialog.Builder(requireContext())
                    .setMessage("품목코드가 일치하지 않습니다.")
                    .setNegativeButton("확인", null)
                    .show()
                if (sw02 == 1) {
                    binding.edtPummokcode.selectAll()
                    binding.edtPummokcode.requestFocus()
                }
                return false
            }
        }

        if (( sw01 == 2 ) || ( sw01 == 3 )) {
            // 수치에 아무것도 없으면 "0"으로 채운다.

            if (binding.edtCount.text.trim().toString() == "" ||
                binding.edtCount.text.trim().toString().isNullOrEmpty()) {
                binding.edtCount.setText("0")
            }
            val inputCount = binding.edtCount.text.trim().toString()
            // 문자 앞에 "0"등이 존재하는 것을 없앤다. 잘못하여 스캐너로 읽힌 경우 숫자앞의 0을 지우는 기능으로 사용
            // (기존에 있다고 입력한 것을 취소하기 위해 0으로 등록할 수 있다.)
            binding.edtCount.setText(inputCount.toInt().toString())

            // 0이 최소수량으로( 취소를 위한 0 입력 가능하도록 )
            if ( 0 > inputCount.toInt() ) {
                binding.edtCount.setText("0")
                AlertDialog.Builder(requireContext())
                    .setMessage("수량에 오류가 있습니다..1")
                    .setNegativeButton("확인", null)
                    .show()

                if (sw02 == 1) {
                    binding.edtCount.selectAll()
                    binding.edtCount.requestFocus()
                }
                return false
            }

//        // 여기서 요청수량과 비교해서 더 크면 요청수량으로 바꾼다.
//        val i_yocheongsuryang = binding.yocheongsuryang.text.trim().toString()
//        if ( inputCount.toInt() > i_yocheongsuryang.toInt() ) {
//            binding.edtCount.setText(binding.yocheongsuryang.text.toString())
//        }
//        // 여기서 1보다 작으면 1 로 바꾼다.
//        inputCount = binding.edtCount.text.trim().toString()
//        if ( 1 > inputCount.toInt() ) {
//            binding.edtCount.setText("1")
//        }

            // 여기서 "요청수량-기출고수량"과 비교해서 더 크면
            // inputCount.toInt() 가 0인 경우도 정상이다. 이화면에 들어오는 조건이 "요청수량-기출고수량"이 1이상인 경우
            val sss1 = (binding.yocheongsuryang.text.trim().toString().toInt()    // 요청수량
                      - binding.gichulgosuryang.text.trim().toString().toInt())   // 기출고수량
            if (inputCount.toInt() > sss1) {
                AlertDialog.Builder(requireContext())
                    .setMessage("입력수량이 요청수량에서 기출고수량을 뺀 수량보다 큽니다..")
                    .setNegativeButton("확인", null)
                    .show()
                if (sw02 == 1) {
                    binding.edtCount.selectAll()
                    binding.edtCount.requestFocus()
                }
                return false
            }
        }
        return true
    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                    // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)   // 액티비티의 onDismiss 를 실행
        }
    }

}