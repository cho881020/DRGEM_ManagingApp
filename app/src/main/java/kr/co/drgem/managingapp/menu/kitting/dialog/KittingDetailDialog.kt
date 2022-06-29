package kr.co.drgem.managingapp.menu.kitting.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogKittingDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
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

    val mSerialDataList = ArrayList<SerialLocalDB>()

    var viewholderCount = 0
    lateinit var pummokData: Pummokdetail
    var mKittingbeonho = ""
    lateinit var tempData: TempData
    val mPummokCode = ""

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

        setValues()
        setupEvents()


    }

    override fun setupEvents() {


        binding.btnAdd.setOnClickListener {


            val contentString = StringBuilder()      //String 문자열 만들기

            for (data in mSerialDataList) {         //시리얼데이터 목록을 돌기 (data 변수 명으로)

                if (data.serial.isNotBlank()) {     // data 의 시리얼이 빈값이 아닐 때

                    contentString.append("${data.serial},")     // contentString 에 시리얼을 담기
                }
            }

            if (contentString.isNotBlank()) {           // contentString 이 빈 값이 아닐 때

                contentString.setLength(contentString.length - 1)           // contentString 길이를 1개 줄임 (, 때문에 빈 값을 제외)

                SerialManageUtil.putSerialStringByPummokCode(
                    "${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}",
                    contentString.toString()
                )        // SerialManageUtil 에 값을 담기 (hashMap 형태로)

                Log.d("품목코드", pummokData.getPummokcodeHP())
                Log.d("저장하는 씨리얼스트링", contentString.toString())
            }


            val inputCount = binding.edtCount.text.trim().toString()
            if(pummokData.getjungyojajeyeobuHP() == "Y"){
                val serialData = SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                    .toString()
                if(inputCount.toInt() != serialData.split(",").size){

                    Log.d("yj", "pummokData.getPummokCount().toInt() : ${pummokData.getPummokCount().toInt()} , serialData.split(\",\").size) : ${serialData.split(",").size}")

                    AlertDialog.Builder(requireContext())
                        .setMessage("입력 수량과 시리얼번호 수량이 일치하지 않습니다..")
                        .setNegativeButton("확인", null)
                        .show()

                    SerialManageUtil.clearData()
                    return@setOnClickListener
                }
            }



            pummokData.setPummokCount(inputCount)
            dismiss()

        }


        binding.btnCancel.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("아직 저장하지 않은 사항이 있습니다.")
                .setMessage("그래도 이 화면을 종료하시겠습니까?")
                .setNeutralButton("예", DialogInterface.OnClickListener { dialog, which ->

                    dismiss()

                })
                .setNegativeButton("아니오", null)
                .show()


        }


        binding.btnOk.setOnClickListener {

            val inputCount = binding.edtCount.text.trim().toString()


            try {
                viewholderCount = inputCount.toInt()
                if (viewholderCount <= 0 ) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("수량을 입력해 주세요.")
                        .setNegativeButton("확인", null)
                        .show()

                    return@setOnClickListener
                }

            } catch (e: Exception) {
                AlertDialog.Builder(requireContext())
                    .setMessage("수량을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()

                return@setOnClickListener
            }


            if (pummokData.jungyojajeyeobu == "Y") {
                binding.txtSerial.isVisible = true
                adapterSet()
            }

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
                    Log.d("yj", "현재고임시등록 code : ${response.body()?.resultcd}")
                    Log.d("yj", "현재고임시등록 msg : ${response.body()?.resultmsg}")
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "현재고임시등록")
                }

            })
            binding.btnAdd.isEnabled = true

        }


        binding.edtPummokcode.setOnEditorActionListener { textView, actionId, keyEvent ->

            val inputPummokCode = binding.edtPummokcode.text.toString()

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.edtPummokcode.onEditorAction(5)

                    if (pummokData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        binding.btnOk.isVisible = true
                        binding.layoutCount.isVisible = true
                        binding.edtCount.requestFocus()


                    } else {
                        AlertDialog.Builder(requireContext())
                            .setMessage("품목코드가 일치하지 않습니다..")
                            .setNegativeButton("확인", null)
                            .show()
                    }

                    return@setOnEditorActionListener true
                }
            }

            return@setOnEditorActionListener actionId != 5
        }

        binding.edtCount.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.edtCount.onEditorAction(5)
                    binding.btnOk.callOnClick()
                    return@setOnEditorActionListener true
                }
            }

            return@setOnEditorActionListener actionId != 5
        }


    }


    override fun setValues() {

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
        binding.edtPummokcode.setText("")
        binding.edtCount.setText("")


        if (pummokData.getPummokCount() != "0") {

            binding.edtPummokcode.setText(pummokData.getPummokcodeHP())
            Log.d("yj", "data.pummokCount : ${pummokData.getPummokcodeHP()} : edtPummokCode ${binding.edtPummokcode}")
            binding.edtCount.setText(pummokData.getPummokCount())

            binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
            binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
            binding.btnOk.isVisible = true
            binding.layoutCount.isVisible = true


            adapterSet()

        }


        Log.d("yj", "binding.edtPummokcode : ${binding.edtPummokcode.text}")
        Log.d("yj", "data.pummokCount : ${pummokData.getPummokCount()}")

    }

    fun adapterSet() {
        var itemCount = 0

        val serialData =
            SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()


        if (serialList.size > viewholderCount) {
            itemCount = serialList.size
        } else if (serialList.size < viewholderCount) {
            itemCount = viewholderCount
        } else {
            itemCount = viewholderCount
        }


        mSerialDataList.clear()


        for (i in 0 until itemCount) {             // 리스트를 뷰 홀더 갯수만큼 만들어서 어댑터로 보내주기

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


        mAdapter = DialogEditKittingAdapter(itemCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter
    }

    fun setCount(kittingbeonho: String, data: Pummokdetail, tempData: TempData) {
        mKittingbeonho = kittingbeonho
//        viewholderCount = count
        pummokData = data
        this.tempData = tempData

    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)           // 액티비티의 onDismiss 를 실행
        }
    }

}