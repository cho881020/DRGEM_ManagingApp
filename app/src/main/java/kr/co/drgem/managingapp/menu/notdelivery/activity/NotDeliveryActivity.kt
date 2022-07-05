/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : NotDeliveryActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 미출자재출고 화면으로 미출고명세요청 및 출고명세요청 기능
 */

package kr.co.drgem.managingapp.menu.notdelivery.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityNotDeliveryBinding
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.adapter.NotDeliveryListAdapter
import kr.co.drgem.managingapp.menu.notdelivery.dialog.NotDeliveryDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.*
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
    val loadingDialog = LoadingDialogFragment()

    var calStart = ""
    var calEnd = ""
    var companyCode = "0002"
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()

    var migwanri = "0"
    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: ArrayList<Detailcode> = arrayListOf()
    var sawonData = ArrayList<SawonData>()
    var ipgodamdangjacode = ""

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: ArrayList<Detailcode> = arrayListOf()

    var calDate = ""

    var SEQ = ""
    var status = "111"
    var sawonCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        binding.chulgodamdangjacode.text = sawonCode

        setupEvents()
        spinnerSet()
        dateSet()

        postRequestNotDelivery()
        sort()
        spinnerSetIn()
        spinnerSetOut()
        completeTextView()
        completeYocheongja()
    }


    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            if (status == "333") {
                backDialog() {
                    workStatusCancle()
                    SerialManageUtil.clearData()
                }
            } else {
                finish()
            }

        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
            binding.btnFold.isVisible = false
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
            binding.btnFold.isVisible = true
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


        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }


        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                migwanri = "0"
            } else {
                migwanri = "1"
            }
        }

        binding.btnFind.setOnClickListener {
            if (status == "111") {
                requestWorkseq()
            } else if (status == "333") {
                status333Dialog() {
                    SerialManageUtil.clearData()
                    requestWorkseq()
                }
            }
        }


    }

    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
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

    //    미출고명세요청
    fun getRequestNotDelivery() {

        val yocheongja = binding.autoCompleteYocheongja.text.toString()
        val yocheongpummok = binding.edtCode.text.toString()

        Log.d("yj", "미출 yocheongja : $yocheongja , yocheongpummok : $yocheongpummok")
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

                        notDeliveryData = it
                        setValues()

                        if (it.returnPummokdetailDetail().size == 0) {
                            searchZeroDialog()
                            setValues()
                            mAdapter.clearList()
                            status = "111"

                        } else {

                            status = "333"
                            binding.layoutList.isVisible = true
                            binding.layoutEmpty.isVisible = false
                        }
                    }
                }
                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<NotDeliveryResponse>, t: Throwable) {
                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                loadingDialog.dismiss()
            }
        })

    }

    //    미출고출고등록
    fun postRequestNotDelivery() {

        binding.btnSave.setOnClickListener {

//            val selecSawon = binding.autoCompleteTextView.text.toString()
//            sawonData.forEach {
//                if(it.sawonmyeong == selecSawon){
//                    ipgodamdangjacode = it.sawoncode
//                    Log.d("yj", "사원명 : $selecSawon , 사원코드 : ${it.sawoncode}")
//                }
//            }

            if(ipgodamdangjacode == ""){
                ipgodamdangjacodeDialog()
                return@setOnClickListener
            }

            notDeliveryData.returnPummokdetailDetail().forEach {

                if (it.getPummokCount() == "0" || it.getPummokCount() == null) {  // 출고수량이 0일때는 체크없음
                    return@forEach
                }

                var serialData =
                    SerialManageUtil.getSerialStringByPummokCode("${it.getpummokcodeHP()}/${it.getyocheongbeonhoHP()}")
                        .toString()      // 시리얼 데이터 꺼내오기

                if (it.jungyojajeyeobu == "Y") {
                    val serialSize = serialData.split(",").size

                    if (serialSize.toString() != it.getPummokCount() || serialData == "null") {
                        countSerialDialog()
                        it.serialCheck = true
                        mAdapter.notifyDataSetChanged()
                        serialData = ""

                        return@setOnClickListener
                    } else {
                        it.serialCheck = false
                        mAdapter.notifyDataSetChanged()
                    }

                }
            }

            saveDialog() {

                val chulgodetail = JsonArray()

                notDeliveryData.returnPummokdetailDetail().forEach {

                    if (it.getPummokCount() == "0" || it.getPummokCount() == null) {
                        return@forEach
                    }

                    val serialData =
                        SerialManageUtil.getSerialStringByPummokCode("${it.getpummokcodeHP()}/${it.getyocheongbeonhoHP()}")
                            .toString()

//                    if (it.jungyojajeyeobu == "Y") {
//
//                        val serialSize = serialData.split(",").size
//
//                        if (serialSize.toString() != it.getSerialCount() || serialData == "null") {
//
//                            Log.d("yj", "시리얼사이즈: ${serialSize}, 입력시리얼${it.getSerialCount()}")
//
//                            countSerialDialog()
//                            it.serialCheck = true
//                            mAdapter.notifyDataSetChanged()
//                            serialData = ""
//
//                            return@saveDialog
//                        } else {
//                            it.serialCheck = false
//                            mAdapter.notifyDataSetChanged()
//                        }
//                    }

                    chulgodetail.add(
                        NotDeliveryChulgodetail(
                            it.getyocheongbeonhoHP(),
                            it.getpummokcodeHP(),
                            it.getPummokCount(),
                            it.getjungyojajeyeobuHP(),
                            serialData
                        ).toJsonObject()
                    )
                }

                val notDeliveryAdd = hashMapOf(
                    "requesttype" to "02072",
                    "chulgoilja" to calDate,
                    "chulgosaupjangcode" to companyCodeOut,
                    "chulgochanggocode" to wareHouseCodeOut,
                    "chulgodamdangjacode" to sawonCode,
                    "ipgosaupjangcode" to companyCodeIn,
                    "ipgochanggocode" to wareHouseCodeIn,
                    "ipgodamdangjacode" to ipgodamdangjacode,
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

                                            status = "111"
                                            SerialManageUtil.clearData()
                                            mAdapter.clearList()
                                            saveDoneDialog()

                                        } else {
                                            serverErrorDialog(it.resultmsg)
                                        }

                                    }
                                }
                            }

                            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                            }

                        })
                } else {
                    saveNotDoneDialog()
                }
            }
        }

    }

    //    작업상태취소
    fun workStatusCancle() {

        val workCancelMap = hashMapOf(
            "requesttype" to "08002",
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

    fun setTempData(): TempData {

        val tempData = TempData(
            companyCodeOut,
            wareHouseCodeOut,
            "",
            SEQ,
            IPUtil.getIpAddress(),
            sawonCode
        )

        return tempData

    }

    override fun setValues() {

        mAdapter =
            NotDeliveryListAdapter(this)
        binding.recyclerView.adapter = mAdapter
        mAdapter.setList(notDeliveryData.returnPummokdetailDetail())
        mAdapter.setTemp(setTempData())

        binding.txtCount.text = "(${notDeliveryData.pummokcount} 건)"

        notDeliveryData.returnPummokdetailDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

        binding.btnSave.isVisible = true
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

                        try {
                            mAdapter.setTemp(setTempData())
                        } catch (e: Exception) {

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
                        try {
                            mAdapter.setTemp(setTempData())
                        } catch (e: Exception) {

                        }
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

    fun sort() {

        var onClickLocation = 0

        binding.layoutLocation.setOnClickListener {

            if (onClickLocation < 2) {
                onClickLocation++
            } else {
                onClickLocation = 0
            }

            when (onClickLocation) {

                0 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(notDeliveryData.returnPummokdetailDetail())
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(notDeliveryData.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(notDeliveryData.getUpLocation())
                }
            }
        }

        var onClickPummyeong = 0

        binding.layoutPummyeong.setOnClickListener {

            if (onClickPummyeong < 2) {
                onClickPummyeong++
            } else {
                onClickPummyeong = 0
            }

            when (onClickPummyeong) {

                0 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(notDeliveryData.returnPummokdetailDetail())
                }

                1 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(notDeliveryData.getDownPummyeong())
                }

                2 -> {
                    binding.imgPummyeong.setImageResource(R.drawable.dropup)
                    mAdapter.setList(notDeliveryData.getUpPummyeong())
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

    private fun completeTextView(){

        val sawonmyeongList = ArrayList<String>()

        SawonDataManager.getSawonDataList()?.let{
            sawonData = it.sawon
        }

        sawonData.forEach {
            sawonmyeongList.add("${it.sawonmyeong} (${it.sawoncode})")
        }

        val autoCompleteTextView = binding.autoCompleteTextView

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sawonmyeongList)

        autoCompleteTextView.setAdapter(adapter)

    }

    private fun completeYocheongja(){

        val sawonmyeongList = ArrayList<String>()

        SawonDataManager.getSawonDataList()?.let{
            sawonData = it.sawon
        }

        sawonData.forEach {
            sawonmyeongList.add("${it.sawonmyeong} (${it.sawoncode})")
        }

        val autoCompleteYocheongja = binding.autoCompleteYocheongja

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sawonmyeongList)

        autoCompleteYocheongja.setAdapter(adapter)

    }


    override fun onBackPressed() {
        if (status == "333") {
            backDialog() {
                workStatusCancle()
                SerialManageUtil.clearData()
            }
        } else {
            finish()
        }

    }

    override fun onClickedEdit(data: PummokdetailDelivery) {

        val dialog = NotDeliveryDialog()
        dialog.setCount(data, setTempData())
        dialog.show(supportFragmentManager, "dialog_notDelivery")

    }

    override fun onDismiss(p0: DialogInterface?) {
        mAdapter.notifyDataSetChanged()
    }

    override fun onItemViewClicked(position: Int) {
        mAdapter.onClickedView(position)
    }

}