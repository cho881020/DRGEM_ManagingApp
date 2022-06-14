package kr.co.drgem.managingapp.menu.request.activity

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
import kr.co.drgem.managingapp.databinding.ActivityRequestDetailBinding
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.adapter.RequestDetailListAdapter
import kr.co.drgem.managingapp.menu.request.dialog.RequestDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RequestDetailActivity : BaseActivity(), RequestDetailEditListener, DialogInterface.OnDismissListener {

    lateinit var binding: ActivityRequestDetailBinding
    lateinit var mAdapter: RequestDetailListAdapter
    lateinit var requestDetailData: RequestDetailResponse

    val dialog = RequestDetailDialog()

    lateinit var mYocheongbeonho: String
    lateinit var SEQ: String

    var johoejogeon = "0"
    var migwanri = "0"
    var companyCode = ""
    var wareHouseCode = ""

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail)

        mYocheongbeonho = intent.getStringExtra("yocheongbeonho").toString()
        SEQ = intent.getStringExtra("seq").toString()
        binding.yocheongbeonho.text = "요청번호 - $mYocheongbeonho"


        setupEvents()
        spinnerSetOut()
        spinnerSetIn()
        dateSet()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
        }

        binding.btnSave.setOnClickListener {
            saveDialog(){
                getPostRequest()
            }
        }

        binding.btnFind.setOnClickListener {
            getRequestDetail()
        }

    }

    override fun setValues() {

        mAdapter = RequestDetailListAdapter(requestDetailData.returnPummokDetail(),this)
        binding.recyclerView.adapter = mAdapter


        binding.txtCount.text = "(${requestDetailData.pummokcount}건)"

        requestDetailData.returnPummokDetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.serialDetail.isVisible = true
            }
        }
    }

    fun getRequestDetail() {

        companyCode = intent.getStringExtra("companyCode").toString()
        wareHouseCode = intent.getStringExtra("wareHouseCode").toString()

        apiList.getRequestRequestDetail( "02062", mYocheongbeonho, johoejogeon, migwanri, companyCode, wareHouseCode).enqueue(object :Callback<RequestDetailResponse>{
            override fun onResponse(
                call: Call<RequestDetailResponse>,
                response: Response<RequestDetailResponse>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        requestDetailData = it

                        setValues()

                    }
                }
            }

            override fun onFailure(call: Call<RequestDetailResponse>, t: Throwable) {

            }

        })

    }

    fun getPostRequest(){

        val chulgodamdangjacode = binding.edtOutName.text.toString()
        val ipgodamdangjacode = binding.edtInName.text.toString()

        val requestChulgodetail : ArrayList<RequestChulgodetail> = arrayListOf()

        requestDetailData.returnPummokDetail().forEach {

            var serialData = SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                .toString()

            if (serialData.isEmpty()) {        // 시리얼 데이터가 빈 값일 경우

                serialData = ""             // "" 으로 표시

            }

            if (serialData != "null") {

                requestChulgodetail.add(
                    RequestChulgodetail(        //check : 요청번호?
                        it.getPummokcodeHP(),
                        serialData.split(",").size.toString(),
                        it.getjungyojajeyeobuHP(),
                        serialData
                    )
                )
            }

        }

        val requestAdd = RequestAdd(
            "02063",
            mYocheongbeonho,
            calDate,
            companyCodeOut,
            wareHouseCodeOut,
            chulgodamdangjacode,
            companyCodeIn,
            wareHouseCodeIn,
            ipgodamdangjacode,
            SEQ,
            "777",
            requestChulgodetail.size.toString(),
            requestChulgodetail
        )

        if (requestChulgodetail.size > 0){
            apiList.postRequestRequestDelivery(requestAdd).enqueue(object : Callback<WorkResponse>{
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
                            }
                            else{
                                Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT).show()
                            }

                            Log.d("yj", "요청출고등록 콜 결과코드 : ${it.resultcd}")
                            Log.d("yj", "요청출고등록 콜 결과메시지 : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "요청출고등록 실패 결과메시지 : ${t.message}")
                }

            })
        }else {
            Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
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
        dialog.setCount(mYocheongbeonho, count, data)
        dialog.show(supportFragmentManager, "dialog_request")
    }

    override fun onBackPressed() {
        backDialog(null)
    }

    override fun onDismiss(p0: DialogInterface?) {

        mAdapter.notifyDataSetChanged()
    }

}