package kr.co.drgem.managingapp.menu.request.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.BaseDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogRequestDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.utils.SerialManageUtil

class RequestDetailDialog : BaseDialogFragment() {

    lateinit var binding: DialogRequestDetailBinding
    lateinit var mAdapter: DialogEditRequestAdapter

    val mSerialDataList = ArrayList<SerialLocalDB>()

    var viewholderCount = 0
    lateinit var pummokData: Pummokdetail
    var mYocheongbeonho = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_request_detail, container, false)
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

            val contentString = StringBuilder()     //String 문자열 만들기

            for (data in mSerialDataList) {         //시리얼데이터 목록을 돌기 (data 변수 명으로)

                if (data.serial.isNotBlank()) {     // data 의 시리얼이 빈값이 아닐 때

                    contentString.append("${data.serial},")     // contentString 에 시리얼을 담기
                }
            }

            if (contentString.isNotBlank()) {           // contentString 이 빈 값이 아닐 때

                contentString.setLength(contentString.length - 1)           // contentString 길이를 1개 줄임 (, 때문에 빈 값을 제외)

                SerialManageUtil.putSerialStringByPummokCode(pummokData.getPummokcodeHP(), contentString.toString())        // SerialManageUtil 에 값을 담기 (hashMap 형태로)

                Log.d("품목코드", pummokData.getPummokcodeHP())
                Log.d("저장하는 씨리얼스트링", contentString.toString())
            }

            saveDoneDialog()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("아직 저장하지 않은 사항이 있습니다.")
                .setMessage("그래도 이 화면을 종료하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                    dismiss()
                })
                .setNegativeButton("아니오", null)
                .show()
        }

    }


    override fun setValues() {

        var itemCount = 0

        val serialData = SerialManageUtil.getSerialStringByPummokCode(pummokData.getPummokcodeHP())
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

        mAdapter = DialogEditRequestAdapter(itemCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.yocheongbeonho.text = mYocheongbeonho
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

    }

    fun setCount(yocheongbeonho: String, count: Int, data: Pummokdetail) {
        mYocheongbeonho = yocheongbeonho
        viewholderCount = count
        pummokData = data

    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)           // 액티비티의 onDismiss 를 실행
        }
    }

}