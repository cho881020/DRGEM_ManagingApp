package kr.co.drgem.managingapp.menu.order.dialog

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
import kr.co.drgem.managingapp.databinding.DialogOrderDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.Baljudetail

class OrderDetailDialog : BaseDialogFragment() {

    lateinit var binding : DialogOrderDetailBinding
    lateinit var mAdapter : DialogEditOrderAdapter

    var viewholderCount = 0
    lateinit var baljuData : Baljudetail
    var mBaljubeonho = ""

    val mSerialDataList = ArrayList<SerialLocalDB>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_order_detail, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValues()
        setupEvents()


    }

    override fun setupEvents(){

        binding.btnAdd.setOnClickListener {

            for (data in mSerialDataList) {

                Log.d("포지션", data.position)
                Log.d("씨리얼", data.serial)

                if (data.serial.isNotBlank()) {

                    mSqliteDB.insertSerialToPummokcode(
                        baljuData.getPummokcodeHP(),
                        data.serial,
                        data.position
                    )
                }

            }

            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }

    override fun setValues(){

        for (i in 0..viewholderCount) {

            val searchedSerial = mSqliteDB.getFirstSerialByPummokcodeAndPosition(baljuData.getPummokcodeHP(), "${i}")

            if (searchedSerial != null) {
                mSerialDataList.add(searchedSerial)
            }
            else {
                mSerialDataList.add(SerialLocalDB(
                    baljuData.pummokcode!!,
                    "",
                    "${i}"
                ))
            }

        }


        mAdapter = DialogEditOrderAdapter(baljuData, viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.baljubeonho.text = mBaljubeonho
        binding.pummokcode.text = baljuData.getPummokcodeHP()
        binding.pummyeong.text = baljuData.getPummyeongHP()
        binding.dobeonModel.text = baljuData.getDobeonModelHP()
        binding.saying.text = baljuData.getSayingHP()
        binding.balhudanwi.text = baljuData.getBalhudanwiHP()
        binding.seq.text = baljuData.getSeqHP()
        binding.jungyojajeyeobu.text = baljuData.getJungyojajeyeobuHP()
        binding.location.text = baljuData.getLocationHP()
        binding.ipgoyejeongil.text = baljuData.getIpgoyejeongilHP()
        binding.baljusuryang.text = baljuData.getBaljusuryangHP()
        binding.ipgosuryang.text = viewholderCount.toString()


    }

    fun setCount ( Baljubeonho : String, count : Int, data : Baljudetail,) {
        mBaljubeonho = Baljubeonho
        viewholderCount = count
        baljuData = data

        Log.d("yj", "setCount $viewholderCount")
        Log.d("yj", "baljuData $baljuData")

    }



}