package kr.co.drgem.managingapp.menu.request.dialog

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
import kr.co.drgem.managingapp.databinding.DialogRequestDetailBinding
import kr.co.drgem.managingapp.models.Pummokdetail

class RequestDetailDialog : DialogFragment() {

    lateinit var binding: DialogRequestDetailBinding
    lateinit var mAdapter: DialogEditRequestAdapter

    var viewholderCount = 0
    lateinit var pummokData : Pummokdetail
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

    fun setupEvents(){

        binding.btnAdd.setOnClickListener {
            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }


    fun setValues() {

        mAdapter = DialogEditRequestAdapter(viewholderCount)
        binding.recyclerView.adapter = mAdapter

        binding.yocheongbeonho.text = mYocheongbeonho
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

    fun setCount (yocheongbeonho : String, count : Int, data : Pummokdetail) {
        mYocheongbeonho = yocheongbeonho
        viewholderCount = count
        pummokData = data

        Log.d("yj", "setCount $viewholderCount")
        Log.d("yj", "pummokData $pummokData")

    }

}