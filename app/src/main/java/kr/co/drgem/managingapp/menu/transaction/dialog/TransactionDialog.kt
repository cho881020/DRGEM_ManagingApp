package kr.co.drgem.managingapp.menu.transaction.dialog

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
import kr.co.drgem.managingapp.databinding.DialogTransactionBinding
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionDialog : DialogFragment() {

    lateinit var binding: DialogTransactionBinding
    lateinit var georaeData: Georaedetail

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

    fun setupEvents() {

        binding.btnAdd.setOnClickListener {
            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }

    fun setValues() {

        val mAdapter = DialogEditTranAdapter(viewholderCount)
        binding.recyclerView.adapter = mAdapter

        binding.baljubeonho.text = georaeData.getBaljubeonhoHP()
        binding.pummokcode.text = georaeData.getPummokcodeHP()
        binding.pummyeong.text = georaeData.getPummyeongHP()
        binding.dobeonModel.text = georaeData.getDobeonModelHP()
        binding.saying.text = georaeData.getSayingHP()
        binding.balhudanwi.text = georaeData.getBalhudanwiHP()
        binding.seq.text = georaeData.getSeqHP()
        binding.jungyojajeyeobu.text = georaeData.getJungyojajeyeobuHP()
        binding.location.text = georaeData.getLocationHP()
        binding.ipgoyejeongil.text = georaeData.getIpgosuryangHP()
        binding.baljusuryang.text = georaeData.getBaljusuryangHP()
        binding.ipgosuryang.text = viewholderCount.toString()

    }

    fun setCount(count: Int, data: Georaedetail) {
        viewholderCount = count
        georaeData = data

        Log.d("yj", "setCount $viewholderCount")
        Log.d("yj", "baljuData $georaeData")

    }
}