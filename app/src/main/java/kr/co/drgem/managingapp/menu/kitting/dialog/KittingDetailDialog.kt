package kr.co.drgem.managingapp.menu.kitting.dialog

import android.app.Activity
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
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogKittingDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.utils.SerialManageUtil

class KittingDetailDialog : DialogFragment() {

    lateinit var binding: DialogKittingDetailBinding
    lateinit var mAdapter: DialogEditKittingAdapter

    val mSerialDataList = ArrayList<SerialLocalDB>()

    var viewholderCount = 0
    lateinit var pummokData : Pummokdetail
    var mKittingbeonho = ""


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

    fun setupEvents(){

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

            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }


    fun setValues() {

        mSerialDataList.clear()

        for (i in 0..viewholderCount) {             // 뷰 홀더 갯수만큼 돌아

            // 리스트를 뷰 홀더 갯수만큼 만들어서 어댑터로 보내주기
            mSerialDataList.add(SerialLocalDB(
                pummokData.pummokcode!!,
                "",
                "${i}"
            ))
        }

        mAdapter = DialogEditKittingAdapter(viewholderCount,mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.kittingbeonho.text = mKittingbeonho
        binding.pummokcode.text = pummokData.getPummokcodeHP()
        binding.pummyeong.text = pummokData.getpummyeongHP()
        binding.dobeonModel.text = pummokData.getdobeon_modelHP()
        binding.saying.text = pummokData.getsayingHP()
        binding.danwi.text = pummokData.getdanwiHP()
        binding.location.text = pummokData.getlocationHP()
        binding.hyeonjaegosuryang.text = pummokData.gethyeonjaegosuryangHP()
        binding.yocheongsuryang.text = pummokData.getyocheongsuryangHP()
        binding.gichulgosuryang.text = pummokData.getgichulgosuryangHP()
        binding.chulgosuryang.text = viewholderCount.toString()
        binding.jungyojajeyeobu.text = pummokData.getjungyojajeyeobuHP()
    }

    fun setCount (kittingbeonho : String, count : Int, data : Pummokdetail) {
        mKittingbeonho = kittingbeonho
        viewholderCount = count
        pummokData = data

        Log.d("yj", "setCount $viewholderCount")
        Log.d("yj", "pummokData $pummokData")

    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)           // 액티비티의 onDismiss 를 실행
        }
    }

}