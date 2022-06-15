package kr.co.drgem.managingapp.menu.notdelivery.activity

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
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityNotDeliveryBinding
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.adapter.NotDeliveryListAdapter
import kr.co.drgem.managingapp.menu.notdelivery.dialog.NotDeliveryDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NotDeliveryActivity : BaseActivity(), NotDeliveryEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityNotDeliveryBinding
    lateinit var mAdapter: NotDeliveryListAdapter
    lateinit var notDeliveryData: NotDeliveryResponse
    val dialog = NotDeliveryDialog()

    var calStart = ""
    var calEnd = ""
    var companyCode = "0002"
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var migwanri = "0"

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    var SEQ = ""
    var status = "111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)

        setupEvents()
        spinnerSet()

        spinnerSetIn()
        spinnerSetOut()


    }


    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(){
                workStatusCancle()
            }
        }

        binding.btnSave.setOnClickListener {
            saveDialog() {
                postRequestNotDelivery()
            }
        }

        val cal = Calendar.getInstance()
        val dateSet = SimpleDateFormat("yyyyMMdd")
        val dateFormat = SimpleDateFormat("MM-dd")

        binding.txtDateStart.text = dateFormat.format(cal.time)
        binding.txtDateEnd.text = dateFormat.format(cal.time)

        calStart = dateSet.format(cal.time)
        calEnd = dateSet.format(cal.time)

        binding.layoutDateStart.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calStart = dateSet.format(cal.time)
                    binding.txtDateStart.text = dateFormat.format(cal.time)

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

        binding.layoutDateEnd.setOnClickListener {
            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calEnd = dateSet.format(cal.time)
                    binding.txtDateEnd.text = dateFormat.format(cal.time)
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

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }


        binding.btnOutName.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInName.setOnClickListener {
            binding.edtInName.text = null
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                migwanri = "0"
            } else {
                migwanri = "1"
            }
        }

        binding.btnFind.setOnClickListener {
            requestWorkseq()
        }


    }

    fun requestWorkseq() {
        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val SEQMap = hashMapOf(
            "requesttype" to "",
            "pid" to "05",
            "tablet_ip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "status" to "111",
        )

        Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")


        apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        if (it.resultcd == "000") {
                            SEQ = it.seq
                            status = "333"

                            getRequestNotDelivery()

                            Log.d("yj", "SEQ : ${it.seq}")
                        } else {
                            Log.d("yj", "SEQ 실패 코드 : ${it.resultmsg}")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                Log.d("yj", "SEQ 서버 실패 : ${t.message}")
            }

        })

    }

    fun getRequestNotDelivery() {

        val yocheongja = binding.edtName.text.toString()
        if (yocheongja.isEmpty()) {
            Toast.makeText(mContext, "요청자를 입력하세요", Toast.LENGTH_SHORT).show()

        }
        var yocheongpummok = binding.edtCode.text.toString()
        if (yocheongpummok.isEmpty()) {
            yocheongpummok = "1"
        }


        apiList.getRequestNotDeliveryDetail(
            "02071",
            calStart,
            calEnd,
            companyCode,
            wareHouseCode,
            yocheongja,
            yocheongpummok,
            migwanri
        ).enqueue(object : Callback<NotDeliveryResponse> {
            override fun onResponse(
                call: Call<NotDeliveryResponse>,
                response: Response<NotDeliveryResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.returnPummokdetailDetail().size == 0) {
                            Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            notDeliveryData = it

                            setValues()

                            binding.layoutList.isVisible = true
                            binding.layoutEmpty.isVisible = false
                        }

                    }


                }

            }

            override fun onFailure(call: Call<NotDeliveryResponse>, t: Throwable) {
                Log.d("yj", "미출고명세 실패 : ${t.message}")
            }

        })


    }

    fun postRequestNotDelivery() {

        val chulgodamdangjacode = binding.edtOutName.text.toString()
        val ipgodamdangjacode = binding.edtInName.text.toString()

        val chulgodetail = JsonArray()

        notDeliveryData.returnPummokdetailDetail().forEach {

            var serialData = SerialManageUtil.getSerialStringByPummokCode(it.getpummokcodeHP())
                .toString()

            if (serialData.isEmpty()) {

                serialData = ""

            }

            if (serialData != "null") {

                chulgodetail.add(
                    NotDeliveryChulgodetail(
                        it.getyocheongbeonhoHP(),
                        it.getpummokcodeHP(),
                        serialData.split(",").size.toString(),
                        it.getjungyojajeyeobuHP(),
                        serialData
                    ).toJsonObject()
                )
            }
        }

        val notDeliveryAdd = hashMapOf(
            "requesttype" to "02072",
            "chulgoilja" to calDate,
            "chulgosaupjangcode" to companyCodeOut,
            "chulgochanggocode" to wareHouseCodeOut,
            "chulgodamdangjacode" to chulgodamdangjacode,
            "ipgosaupjangcode" to companyCodeIn,
            "ipgodamdangjacode" to wareHouseCodeIn,
            "ipgochanggocode" to ipgodamdangjacode,
            "seq" to SEQ,
            "status" to "777",
            "pummokcount" to chulgodetail.size().toString(),
            "chulgodetail" to chulgodetail
        )

        if (chulgodetail.size() > 0) {
            apiList.postRequestNotDeliveryDelivery(notDeliveryAdd)
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

                                Log.d("yj", "미출고등록 콜 결과코드 : ${it.resultcd}")
                                Log.d("yj", "미출고등록 콜 결과메시지 : ${it.resultmsg}")

                            }
                        }
                    }

                    override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                        Log.d("yj", "미출고등록 실패 결과메시지 : ${t.message}")
                    }

                })
        } else {
            Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun workStatusCancle() {

        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val workCancelMap = hashMapOf(
            "requesttype" to "",
            "seq" to SEQ,
            "tablet_ip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "status" to status,
        )

        apiList.postRequestWorkstatusCancle(workCancelMap)
            .enqueue(object : Callback<WorkResponse> {
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {

                            Log.d("yj", "거래 작업상태취소 code : ${it.resultcd}")
                            Log.d("yj", "거래 작업상태취소 msg : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "발주 작업상태취소 실패 : ${t.message}")
                }

            })

    }


    override fun setValues() {

        mAdapter = NotDeliveryListAdapter(notDeliveryData.returnPummokdetailDetail(), this)
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${notDeliveryData.pummokcount} 건)"

        notDeliveryData.returnPummokdetailDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

    }

    fun spinnerSet() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompany.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

            binding.spinnerCompany.setSelection(1)

            binding.spinnerCompany.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCode = "0001"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouse.setSelection(0, false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }

                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCode = "0002"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGumiCode())
                            binding.spinnerWareHouse.setSelection(0, false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }

                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouse.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCode = mWareHouseList[position].code
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

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

    override fun onBackPressed() {
        backDialog(){
            workStatusCancle()
        }
    }

    override fun onClickedEdit(count: Int, data: PummokdetailDelivery) {
        dialog.setCount(count, data)
        dialog.show(supportFragmentManager, "dialog_notDelivery")

    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()
    }

}