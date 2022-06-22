package kr.co.drgem.managingapp.menu.transaction.dialog

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
import kr.co.drgem.managingapp.databinding.DialogTransactionBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.utils.SerialManageUtil

class TransactionDialog : BaseDialogFragment() {

    lateinit var binding: DialogTransactionBinding
    lateinit var georaeData: Georaedetail

    val mSerialDataList = ArrayList<SerialLocalDB>()

    var viewholderCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_transaction, container, false)
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

                SerialManageUtil.putSerialStringByPummokCode(georaeData.getPummokcodeHP(), contentString.toString())        // SerialManageUtil 에 값을 담기 (hashMap 형태로)

                Log.d("품목코드", georaeData.getPummokcodeHP())
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

        val serialData = SerialManageUtil.getSerialStringByPummokCode(georaeData.getPummokcodeHP())
            .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()


        if (serialList.size > viewholderCount) {
            itemCount = serialList.size
        } else if (serialList.size < viewholderCount) {
            itemCount = viewholderCount
        } else {
            itemCount = viewholderCount
        }


        /**
         *  serial데이터가 있다면, 시리얼을 목록에 담고,
         *  없다면 그때 빈값으로 만들 수 있도록
         */

        mSerialDataList.clear()


        for (i in 0 until itemCount) {             // 리스트를 뷰 홀더 갯수만큼 만들어서 어댑터로 보내주기

            var serial = ""

            if (serialList.isNotEmpty() && serialList.size > i) {      // 시리얼리스트가 사이즈 i보다 크거나 같을 때

                serial = serialList[i]

            }

            mSerialDataList.add(
                SerialLocalDB(
                    georaeData.pummokcode!!,
                    serial,
                    "${i}"
                )
            )
        }


        val mAdapter = DialogEditTranAdapter(itemCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.baljubeonho.text = georaeData.getBaljubeonhoHP()
        binding.pummokcode.text = georaeData.getPummokcodeHP()
        binding.pummyeong.text = georaeData.getPummyeongHP()
        binding.dobeonModel.text = georaeData.getDobeonModelHP()
        binding.sayang.text = georaeData.getsayangHP()
        binding.balhudanwi.text = georaeData.getBalhudanwiHP()
        binding.seq.text = georaeData.getSeqHP()
        binding.jungyojajeyeobu.text = georaeData.getJungyojajeyeobuHP()
        binding.location.text = georaeData.getLocationHP()
        binding.ipgoyejeongil.text = "-"
        binding.baljusuryang.text = georaeData.getBaljusuryangHP()
        binding.ipgosuryang.text = viewholderCount.toString()

    }

    fun setCount(count: Int, data: Georaedetail) {
        viewholderCount = count
        georaeData = data

    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)           // 액티비티의 onDismiss 를 실행
        }
    }

}