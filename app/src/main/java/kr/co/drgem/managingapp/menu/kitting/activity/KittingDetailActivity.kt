package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityKittingDetailBinding
    lateinit var mAdapter: KittingDetailListAdapter
    lateinit var kittingDetailData: KittingDetailResponse

    val dialog = KittingDetailDialog()

    lateinit var mkittingbeonho: String
    lateinit var seq: String

    var johoejogeon = "0"
    var migwanri = "0"
    var changgocode = ""

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting_detail)

        mkittingbeonho = intent.getStringExtra("kittingbeonho").toString()
        seq = intent.getStringExtra("seq").toString()
        binding.kittingbeonho.text = "키팅번호 - $mkittingbeonho"

        setupEvents()
        spinnerSetOut()
        spinnerSetIn()
        dateSet()
        postRequestKitting()
    }

    override fun onBackPressed() {
        backDialog(null)
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
        }



        binding.btnOutNameRemove.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInNameRemove.setOnClickListener {
            binding.edtInName.text = null
        }

        binding.btnFind.setOnClickListener {
            getRequestKittingDetail()
        }

    }

    override fun setValues() {

        mAdapter = KittingDetailListAdapter(kittingDetailData.returnKittingDetail(), this)
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${kittingDetailData.getPummokCount()} 건)"


        kittingDetailData.returnKittingDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }


    }


    fun getRequestKittingDetail() {

        apiList.getRequestKittingDetail("02502", mkittingbeonho, johoejogeon, migwanri, changgocode)
            .enqueue(object : Callback<KittingDetailResponse> {
                override fun onResponse(
                    call: Call<KittingDetailResponse>,
                    response: Response<KittingDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            kittingDetailData = it

                            setValues()

                        }
                    }
                }

                override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                    Log.d("yj", "키팅명세요청실패 : ${t.message}")
                }

            })

    }

    fun postRequestKitting() {

        binding.btnSave.setOnClickListener {
            saveDialog() {
                val chulgodamdangjacode = binding.edtOutName.text.toString()
                val ipgodamdangjacode = binding.edtInName.text.toString()

                val chulgodetail: ArrayList<KittingChulgodetail> = arrayListOf()


                kittingDetailData.returnKittingDetail().forEach {

                    var serialData = SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                        .toString()      // 시리얼 데이터 꺼내오기

                    if(serialData == "null"){
                        serialData = ""
                    }

                    if (serialData.isNotEmpty()) {        // 시리얼 데이터가 null아닐때만
                        val serialSize = serialData.split(",").size

                        Log.d("yj", "serialDataSize : $serialSize")
                        Log.d("yj", "serialData : $serialData")

                        Log.d("yj", "시리얼입력수량 : ${it.getSerialCount()}")

                        if(serialSize > 0){
                            if(serialSize.toString() != it.getSerialCount()){
                                Toast.makeText(mContext, "입력 수량과 시리얼넘버 수량이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                it.serialCheck = true
                                mAdapter.notifyDataSetChanged()
                                serialData = ""

                                return@saveDialog
                            }
                            else {
                                it.serialCheck = false
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }


                    if (serialData.isNotEmpty()) {

                        chulgodetail.add(
                            KittingChulgodetail(
                                "000",         //check : 요청번호?
                                it.getPummokcodeHP(),
                                serialData.split(",").size.toString(),
                                it.getjungyojajeyeobuHP(),
                                serialData
                            )
                        )
                    }
                }

                val kittingAdd = KittingAdd(
                    "02053",
                    mkittingbeonho,
                    calDate,
                    companyCodeOut,
                    wareHouseCodeOut,
                    chulgodamdangjacode,
                    companyCodeIn,
                    wareHouseCodeIn,
                    ipgodamdangjacode,
                    seq,
                    "777",
                    chulgodetail.size.toString(),
                    chulgodetail
                )


                Log.d("yj", "일괄출고등록 맵확인 : $kittingAdd")

                if (chulgodetail.size > 0) {
                    apiList.postRequestDeliveryBatch(kittingAdd)
                        .enqueue(object : Callback<WorkResponse> {
                            override fun onResponse(
                                call: Call<WorkResponse>,
                                response: Response<WorkResponse>
                            ) {

                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.resultcd == "000") {

                                            SerialManageUtil.clearData()
                                            mAdapter.notifyDataSetChanged()

                                            Toast.makeText(mContext, "저장이 완료되었습니다.", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                        Log.d("yj", "일괄출고등록 콜 결과코드 : ${it.resultcd}")
                                        Log.d("yj", "일괄출고등록 콜 결과메시지 : ${it.resultmsg}")

                                    }
                                }
                            }

                            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {

                                Log.d("yj", "일괄출고등록 실패 결과메시지 : ${t.message}")
                            }

                        })


                } else {
                    Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
                }

            }
        }



    }

    fun spinnerSetOut() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyOut.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseOut.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeOut = "0001"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)
                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeOut = "0002"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGumiCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)

                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeOut = mWareHouseListOut[position].code

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    fun spinnerSetIn() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyIn.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseIn.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeIn = "0001"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)
                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeIn = "0002"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGumiCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)

                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeIn = mWareHouseListIn[position].code

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    fun dateSet() {
        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        calDate = dateServer.format(cal.time)
        binding.layoutDate.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calDate = dateServer.format(cal.time)
                    binding.txtDate.text = dateFormat.format(cal.time)

                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()
        }

    }

    override fun onClickedEdit(count: Int, data: Pummokdetail) {

        dialog.setCount(mkittingbeonho, count, data)
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()             // 어댑터 데이터 변경 (시리얼이 담긴 리스트 버튼 컬러 변경)
    }
}