package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityOrderDetailBinding
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener,
    DialogInterface.OnDismissListener {

    lateinit var binding: ActivityOrderDetailBinding
    lateinit var mAdapter: OrderDetailListAdapter
    lateinit var orderDetailData: OrderDetailResponse

    val dialog = OrderDetailDialog()
    val baljuDetail = ArrayList<Baljudetail>()
    lateinit var mBaljubeonho: String


    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var companyCode = "0001"
    var wareHouseCode = "1001"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)

        mBaljubeonho = intent.getStringExtra("baljubeonho").toString()


        setupEvents()
        setValues()
        spinnerSet()
        getRequestOrderDetail()


    }

    override fun onBackPressed() {
        backDialog()
    }

    override fun setupEvents() {


        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnBack.setOnClickListener {
            backDialog()
        }

        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = dateServer.format(cal.time)
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

        binding.btnSave.setOnClickListener {

            saveDialog()

            val ipgodetail = ArrayList<IpgodetaildetailAdd>()   // 등록용 리스트
            val inputName = binding.edtName.text.toString()


            orderDetailData.returnBaljudetail().forEach {

                val serialdata =
                    SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP()).toString()

                Log.d("yj", "serialData : $serialdata")

                if(serialdata.isNotEmpty()){        // null값 까지 같이 들어옴

                    ipgodetail.add(
                        IpgodetaildetailAdd(
                            it.getSeqHP(),
                            it.getPummokcodeHP(),
                            serialdata.split(",").size.toString(),
                            it.getJungyojajeyeobuHP(),
                            serialdata
                        )
                    )
                }
            }

            val georaeMap = hashMapOf(
                "requesttype" to "02014",
                "baljubeonho" to mBaljubeonho,
                "ipgoilja" to calDate,
                "ipgosaupjangcode" to companyCode,
                "ipgochanggocode" to wareHouseCode,
                "ipgodamdangja" to inputName,
                "georaecheocode" to orderDetailData.georaecheocode,
                "pummokcount" to ipgodetail.size,
                "ipgodetail" to ipgodetail
            )

            Log.d("yj", "ipgodetail size : ${ipgodetail.size}")
            ipgodetail.forEach {
                Log.d("yj", "ipgodetail : $it")
            }
        }
    }

    override fun setValues() {

        mAdapter = OrderDetailListAdapter(this, mContext, baljuDetail)
        binding.recyclerView.adapter = mAdapter


    }

    fun spinnerSet() {
        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(
                mContext,
                R.layout.spinner_list_item,
                masterData.getCompanyCode()
            )
        binding.spinnerCompany.adapter = spinnerCompanyAdapter


        val spinnerWareHouseAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
        binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter


        binding.spinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                        companyCode = "0001"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGwangmyeongCode())

                        if (mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }

                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                        companyCode = "0002"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGumiCode())

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

    fun setOrderDetailDataToUI() {

        binding.baljubeonho.text = "발주번호 - $mBaljubeonho"
        binding.baljubeonho2.text = mBaljubeonho

        binding.baljuil.text = orderDetailData.getBaljuilHP()
        binding.georaecheocode.text = orderDetailData.getGeoraecheocodeHP()
        binding.georaecheomyeong.text = orderDetailData.getGeoraecheomyeongHP()
        binding.bigo.text = orderDetailData.getBigoHP()
        binding.txtCount.text = "(${baljuDetail.size}건)"

        baljuDetail.forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }

        SerialManageUtil.clearData()

        for (pummok in baljuDetail) {
            val serialList = mSqliteDB.getAllSerialByPummokcode(pummok.getPummokcodeHP())


            val contentString = StringBuilder()
            for (data in serialList) {

                if (data.serial.isNotBlank()) {
                    contentString.append("${data.serial},")
                }

            }
            if (contentString.length > 1) {
                contentString.setLength(contentString.length - 1)
                SerialManageUtil.putSerialStringByPummokCode(
                    pummok.getPummokcodeHP(),
                    contentString.toString()
                )
            }


        }

        mAdapter.notifyDataSetChanged()

    }

    fun getRequestOrderDetail() {

        val savedOrderDetailList = mSqliteDB.getSavedOrderDetail()
        if (savedOrderDetailList.size > 0 && savedOrderDetailList[0].baljubeonho == mBaljubeonho) {
            orderDetailData = savedOrderDetailList[0]
            baljuDetail.clear()

            for (detail in orderDetailData.returnBaljudetail()) {
                baljuDetail.add(detail)
            }


            setOrderDetailDataToUI()
        } else {
            apiList.getRequestOrderDetail("02012", mBaljubeonho)
                .enqueue(object : Callback<OrderDetailResponse> {
                    override fun onResponse(
                        call: Call<OrderDetailResponse>,
                        response: Response<OrderDetailResponse>
                    ) {

                        response.body()?.let {

                            orderDetailData = it
                            clearAndSaveDataToDB()

                            baljuDetail.clear()
                            baljuDetail.addAll(it.returnBaljudetail())

                            setOrderDetailDataToUI()

                        }
                    }

                    override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {

                        Log.d("yj", "OrderDetail 실패 : ${t.message}")
                    }

                })
        }


    }

    fun clearAndSaveDataToDB() {

        mSqliteDB.deleteOrderDetail()
        mSqliteDB.deleteAllSerials()

        mSqliteDB.insertOrderDetail(orderDetailData)

    }

    override fun onClickedEdit(count: Int, data: Baljudetail) {

        dialog.setCount(mBaljubeonho, count, data)
        dialog.show(supportFragmentManager, "EditDialog")
        supportFragmentManager.executePendingTransactions()


//        dialog.dialog?.setOnDismissListener(this)
    }

    override fun onDismiss(p0: DialogInterface?) {

        Log.d("다이얼로그닫히면", "로그찍히나")

        mAdapter.notifyDataSetChanged()
    }

}