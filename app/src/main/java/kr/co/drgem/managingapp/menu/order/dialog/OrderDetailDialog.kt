package kr.co.drgem.managingapp.menu.order.dialog

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
import kr.co.drgem.managingapp.BaseDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogOrderDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.utils.SerialManageUtil


class OrderDetailDialog : BaseDialogFragment() {

    lateinit var binding: DialogOrderDetailBinding
    lateinit var mAdapter: DialogEditOrderAdapter

    var viewholderCount = 0
    lateinit var baljuData: Baljudetail
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

    override fun setupEvents() {

        binding.btnAdd.setOnClickListener {

            mSqliteDB.deletePummokcodeSerials(baljuData.getPummokcodeHP())

            val contentString = StringBuilder()

            for (data in mSerialDataList) {

                if (data.serial.isNotBlank()) {

                    mSqliteDB.insertSerialToPummokcode(
                        baljuData.getPummokcodeHP(),
                        data.serial,
                        data.position
                    )

                    contentString.append("${data.serial},")
                }

            }
            if (contentString.isNotBlank()) {

                contentString.setLength(contentString.length - 1)

                SerialManageUtil.putSerialStringByPummokCode(
                    baljuData.getPummokcodeHP(),
                    contentString.toString()
                )

                Log.d("품목코드", baljuData.getPummokcodeHP())
                Log.d("저장하는 씨리얼스트링", contentString.toString())
            }

            Toast.makeText(requireContext(), "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show()
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

        val serialData = SerialManageUtil.getSerialStringByPummokCode(baljuData.getPummokcodeHP())
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
                    baljuData.pummokcode!!,
                    serial,
                    "${i}"
                )
            )
        }


        mAdapter = DialogEditOrderAdapter(baljuData, itemCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

        binding.baljubeonho.text = mBaljubeonho
        binding.pummokcode.text = baljuData.getPummokcodeHP()
        binding.pummyeong.text = baljuData.getPummyeongHP()
        binding.dobeonModel.text = baljuData.getDobeonModelHP()
        binding.sayang.text = baljuData.getsayangHP()
        binding.balhudanwi.text = baljuData.getBalhudanwiHP()
        binding.seq.text = baljuData.getSeqHP()
        binding.jungyojajeyeobu.text = baljuData.getJungyojajeyeobuHP()
        binding.location.text = baljuData.getLocationHP()
        binding.ipgoyejeongil.text = baljuData.getIpgoyejeongilHP()
        binding.baljusuryang.text = baljuData.getBaljusuryangHP()
        binding.ipgosuryang.text = viewholderCount.toString()


    }

    fun setCount(Baljubeonho: String, count: Int, data: Baljudetail) {
        mBaljubeonho = Baljubeonho
        viewholderCount = count
        baljuData = data

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}