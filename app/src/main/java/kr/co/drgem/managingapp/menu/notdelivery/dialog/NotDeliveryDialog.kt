package kr.co.drgem.managingapp.menu.notdelivery.dialog

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
import kr.co.drgem.managingapp.databinding.DialogNotDeliveryBinding
import kr.co.drgem.managingapp.databinding.DialogTransactionBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.PummokdetailDelivery
import kr.co.drgem.managingapp.utils.SerialManageUtil

class NotDeliveryDialog : DialogFragment() {

    lateinit var binding: DialogNotDeliveryBinding
    lateinit var pummokData : PummokdetailDelivery


    val mSerialDataList = ArrayList<SerialLocalDB>()
    var viewholderCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_not_delivery, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        setValues()

    }

    fun setupEvents(){

        binding.btnAdd.setOnClickListener {

            val contentString = StringBuilder()

            for (data in mSerialDataList) {

                if (data.serial.isNotBlank()) {

                    contentString.append("${data.serial},")
                }
            }

            if (contentString.isNotBlank()) {

                contentString.setLength(contentString.length - 1)

                SerialManageUtil.putSerialStringByPummokCode(pummokData.getpummokcodeHP(), contentString.toString())

            }

            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
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
            mSerialDataList.add(
                SerialLocalDB(
                    pummokData.pummokcode!!,
                "",
                "${i}"
            )
            )
        }

        val mAdapter = DialogEditNotDeliveryAdapter(viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.yocheongbeonho.text = pummokData.getyocheongbeonhoHP()
        binding.yocheongil.text = pummokData.getyocheongilHP()
        binding.yocheongchanggo.text = pummokData.getyocheongchanggoHP()
        binding.yocheongja.text = pummokData.getyocheongjaHP()
        binding.pummokcode.text = pummokData.getpummyeongHP()
        binding.pummyeong.text = pummokData.getpummyeongHP()
        binding.dobeonModel.text = pummokData.getdobeon_modelHP()
        binding.saying.text = pummokData.getsayingHP()
        binding.balhudanwi.text = pummokData.getdanwiHP()
        binding.location.text = pummokData.getlocationHP()

        binding.hyeonjaegosuryang.text = pummokData.gethyeonjaegosuryangHP()
        binding.yocheongsuryang.text = pummokData.getyocheongsuryangHP()
        binding.gichulgosuryang.text = pummokData.getgichulgosuryangHP()
        binding.chulgosuryang.text = viewholderCount.toString()
        binding.jungyojajeyeobu.text = pummokData.getjungyojajeyeobuHP().toString()

    }

    fun setCount (count : Int, data : PummokdetailDelivery) {
        viewholderCount = count
        pummokData = data

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }


}