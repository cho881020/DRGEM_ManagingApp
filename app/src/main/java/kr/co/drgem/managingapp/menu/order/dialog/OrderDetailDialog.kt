package kr.co.drgem.managingapp.menu.order.dialog

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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.DialogOrderDetailBinding
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailDialog : BaseDialogFragment() {

    lateinit var binding: DialogOrderDetailBinding
    lateinit var mAdapter: DialogEditOrderAdapter

    var viewholderCount = 0
    lateinit var baljuData: Baljudetail
    var mBaljubeonho = ""
    lateinit var tempData: TempData

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

            var inputCount = binding.edtCount.text.trim().toString()

            if (inputCount.isNullOrEmpty() || inputCount == "") {
                inputCount = "0"
            }
            val tempMap = hashMapOf(
                "requesttype" to "08003",
                "saeopjangcode" to tempData.saeopjangcode,
                "changgocode" to tempData.changgocode,
                "pummokcode" to baljuData.getPummokcodeHP(),
                "suryang" to inputCount,
                "yocheongbeonho" to mBaljubeonho,
                "ipchulgubun" to "1",
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
                    Log.d("yj", "????????????????????? code : ${response.body()?.resultcd}")
                    Log.d("yj", "????????????????????? msg : ${response.body()?.resultmsg}")
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "?????????????????????")
                }

            })

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

                Log.d("????????????", baljuData.getPummokcodeHP())
                Log.d("???????????? ??????????????????", contentString.toString())
            }



            if(baljuData.getJungyojajeyeobuHP() == "Y"){
                val serialData = SerialManageUtil.getSerialStringByPummokCode(baljuData.getPummokcodeHP())
                    .toString()

                try{
                    if(inputCount == "0"){
                        SerialManageUtil.clearData()
                    }
                    else if(inputCount.toInt() != serialData.split(",").size){

                        Log.d("yj", "inputCount : ${inputCount.toInt()} , serialData.split(\",\").size) : ${serialData.split(",").size}")

                        AlertDialog.Builder(requireContext())
                            .setMessage("?????? ????????? ??????????????? ????????? ???????????? ????????????.")
                            .setNegativeButton("??????", null)
                            .show()

                        SerialManageUtil.clearData()
                        return@setOnClickListener
                    }
                }
                catch (e: Exception) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("????????? ????????? ?????????.")
                        .setNegativeButton("??????", null)
                        .show()

                    return@setOnClickListener
                }

            }

            baljuData.setPummokCount(inputCount)
            saveDoneDialog()
            dismiss()

        }

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("?????? ???????????? ?????? ????????? ????????????.")
                .setMessage("????????? ??? ????????? ?????????????????????????")
                .setNeutralButton("???", DialogInterface.OnClickListener { dialog, which ->

                    dismiss()
                })
                .setNegativeButton("?????????", null)
                .show()

        }

        binding.btnOk.setOnClickListener {

            val inputCount = binding.edtCount.text.trim().toString()


            try {
                viewholderCount = inputCount.toInt()


            } catch (e: Exception) {
                AlertDialog.Builder(requireContext())
                    .setMessage("????????? ????????? ?????????.")
                    .setNegativeButton("??????", null)
                    .show()

                return@setOnClickListener
            }


            if (baljuData.jungyojajeyeobu == "Y") {
                adapterSet()
            }

        }


        binding.edtPummokcode.setOnEditorActionListener { textView, actionId, keyEvent ->

            val inputPummokCode = binding.edtPummokcode.text.toString()

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.edtPummokcode.onEditorAction(5)

                    if (baljuData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        if (baljuData.getJungyojajeyeobuHP() == "Y") {
                            binding.btnOk.isVisible = true
                        }
                        binding.layoutCount.isVisible = true
                        binding.edtCount.requestFocus()


                    } else {
                        AlertDialog.Builder(requireContext())
                            .setMessage("??????????????? ???????????? ????????????.")
                            .setNegativeButton("??????", null)
                            .show()

                        binding.edtPummokcode.requestFocus()
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

        if (baljuData.getJungyojajeyeobuHP() == "Y") {
            binding.layoutSerial.isVisible = true

            binding.edtPummokcode.setText("")
            binding.edtCount.setText("")


            if (baljuData.getPummokCount() != "0") {

//                binding.edtPummokcode.setText(baljuData.getPummokcodeHP())
                Log.d(
                    "yj",
                    "data.pummokCount : ${baljuData.getPummokcodeHP()} : edtPummokCode ${binding.edtPummokcode}"
                )
                binding.edtCount.setText(baljuData.getPummokCount())

                binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                binding.layoutCount.isVisible = true
                if (baljuData.getJungyojajeyeobuHP() == "Y") {
                    binding.btnOk.isVisible = true
                    adapterSet()
                }

            }
        }

    }

    fun adapterSet(){

        val serialData = SerialManageUtil.getSerialStringByPummokCode(baljuData.getPummokcodeHP())
            .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()



        mSerialDataList.clear()


        for (i in 0 until viewholderCount) {             // ???????????? ??? ?????? ???????????? ???????????? ???????????? ????????????

            var serial = ""

            if (serialList.isNotEmpty() && serialList.size > i) {      // ????????????????????? ????????? i?????? ????????? ?????? ???

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


        mAdapter = DialogEditOrderAdapter(baljuData, viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter

    }

    fun setCount(Baljubeonho: String, data: Baljudetail, tempData: TempData) {
        mBaljubeonho = Baljubeonho
        baljuData = data
        this.tempData = tempData

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}