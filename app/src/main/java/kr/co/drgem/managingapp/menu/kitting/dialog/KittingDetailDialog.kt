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

        binding.btnCodeRemove.setOnClickListener {
            binding.edtPummokcode.text = null
        }


        binding.btnAdd.setOnClickListener {

            var inputCount = binding.edtCount.text.trim().toString()

            if (inputCount.isNullOrEmpty() || inputCount == "") {

                inputCount = "0"
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
                    Log.d("yj", "????????????????????? code : ${response.body()?.resultcd}")
                    Log.d("yj", "????????????????????? msg : ${response.body()?.resultmsg}")
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "?????????????????????")
                }

            })


            val contentString = StringBuilder()      //String ????????? ?????????

            for (data in mSerialDataList) {         //?????????????????? ????????? ?????? (data ?????? ?????????)

                if (data.serial.isNotBlank()) {     // data ??? ???????????? ????????? ?????? ???

                    contentString.append("${data.serial},")     // contentString ??? ???????????? ??????
                }
            }

            if (contentString.isNotBlank()) {           // contentString ??? ??? ?????? ?????? ???

                contentString.setLength(contentString.length - 1)           // contentString ????????? 1??? ?????? (, ????????? ??? ?????? ??????)

                SerialManageUtil.putSerialStringByPummokCode(
                    "${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}",
                    contentString.toString()
                )        // SerialManageUtil ??? ?????? ?????? (hashMap ?????????)

                Log.d("????????????", pummokData.getPummokcodeHP())
                Log.d("???????????? ??????????????????", contentString.toString())
            }



            if (pummokData.getjungyojajeyeobuHP() == "Y") {
                val serialData =
                    SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                        .toString()

                try {
                    if(inputCount == "0"){
                        SerialManageUtil.clearData()
                    }
                    else if(inputCount.toInt() != serialData.split(",").size){

                        AlertDialog.Builder(requireContext())
                            .setMessage("?????? ????????? ??????????????? ????????? ???????????? ????????????..")
                            .setNegativeButton("??????", null)
                            .show()

                        SerialManageUtil.clearData()
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("????????? ????????? ?????????.")
                        .setNegativeButton("??????", null)
                        .show()

                    return@setOnClickListener
                }

            }

            pummokData.setPummokCount(inputCount)
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
            if (pummokData.jungyojajeyeobu == "Y") {
                adapterSet()
            }

        }


        binding.edtPummokcode.setOnEditorActionListener { textView, actionId, keyEvent ->

            val inputPummokCode = binding.edtPummokcode.text.toString()

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    binding.edtPummokcode.onEditorAction(5)

                    if (pummokData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        if (pummokData.getjungyojajeyeobuHP() == "Y") {
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

            if (actionId == 5) {
                    if (pummokData.getPummokcodeHP() == inputPummokCode) {
                        binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
                        binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
                        if (pummokData.getjungyojajeyeobuHP() == "Y") {
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

            return@setOnEditorActionListener actionId != 5
        }

        binding.edtCount.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {

                // ?????? ?????? "0"?????? ???????????? ?????? ?????????.// by jung 2022.07.01
                val inputCount = binding.edtCount.text.trim().toString()
                binding.edtCount.setText(inputCount.toInt().toString())
                // ????????? ??????????????? ???????????? ??? ?????? ?????????????????? ?????????.  // by jung 2022.07.01
                val i_yocheongsuryang = binding.yocheongsuryang.text.trim().toString()
                if ( inputCount.toInt() > i_yocheongsuryang.toInt() ) {
                    binding.edtCount.setText(binding.yocheongsuryang.text.toString())
                }

                if (keyEvent.action == KeyEvent.ACTION_UP) {

                    // ?????? ?????? ??????????????? ???????????? ????????? ??????.   // by jung 2022.07.01
                    // ?????? ?????? ?????? ????????? ?????????????????? ????????? ??????.
                    binding.edtCount.selectAll()
                    binding.edtCount.requestFocus()


                    //binding.edtCount.onEditorAction(5)  // by jung 2022.07.01 ????????? ????????? ??????
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
        if (pummokData.getjungyojajeyeobuHP() == "Y") {
            binding.layoutSerial.isVisible = true
        }

        binding.edtPummokcode.setText("")
        binding.edtCount.setText("")


        if (pummokData.getPummokCount() != "0") {

            Log.d(
                "yj",
                "data.pummokCount : ${pummokData.getPummokcodeHP()} : edtPummokCode ${binding.edtPummokcode}"
            )
            binding.edtCount.setText(pummokData.getPummokCount())

            binding.edtPummokcode.setBackgroundResource(R.drawable.gray_box)
            binding.edtPummokcode.setTextColor(requireContext().resources.getColor(R.color.color_808080))
            binding.layoutCount.isVisible = true
            if (pummokData.getjungyojajeyeobuHP() == "Y") {
                binding.btnOk.isVisible = true
                adapterSet()
            }
        }


        Log.d("yj", "binding.edtPummokcode : ${binding.edtPummokcode.text}")
        Log.d("yj", "data.pummokCount : ${pummokData.getPummokCount()}")

    }

    fun adapterSet() {

        val serialData =
            SerialManageUtil.getSerialStringByPummokCode("${pummokData.getPummokcodeHP()}/${pummokData.getyocheongbeonhoHP()}")
                .toString()
        val serialList = if (serialData != "null") serialData.split(",") else arrayListOf()


        mSerialDataList.clear()


        for (i in 0 until viewholderCount) {             // ???????????? ??? ?????? ???????????? ???????????? ???????????? ????????????

            var serial = ""

            if (serialList.isNotEmpty()                                                                                                                                                                                                                                                                                                          && serialList.size > i) {      // ????????????????????? ????????? i?????? ????????? ?????? ???

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


        mAdapter = DialogEditKittingAdapter(viewholderCount, mSerialDataList)
        binding.recyclerView.adapter = mAdapter
    }

    fun setCount(kittingbeonho: String, data: Pummokdetail, tempData: TempData) {
        mKittingbeonho = kittingbeonho
        pummokData = data
        this.tempData = tempData

    }

    override fun onDismiss(dialog: DialogInterface) {           // ?????????????????? ?????? ??? ????????????????????? ??????????????? ?????????
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // ??????????????? ??????????????? ???????????? ?????????,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)           // ??????????????? onDismiss ??? ??????
        }
    }



}