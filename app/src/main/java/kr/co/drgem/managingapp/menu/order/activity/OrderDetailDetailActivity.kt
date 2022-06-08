package kr.co.drgem.managingapp.menu.order.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import kr.co.drgem.managingapp.models.BaljuData
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.MasterDataResponse
import kr.co.drgem.managingapp.models.OrderDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener {

    lateinit var binding: ActivityOrderDetailBinding
    lateinit var mAdapter: OrderDetailListAdapter
    lateinit var orderDetailData: OrderDetailResponse

    val dialog = OrderDetailDialog()
    val baljuDetail = ArrayList<Baljudetail>()

    val mBaljuList = ArrayList<BaljuData>()

    lateinit var mBaljubeonho: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)

        mBaljubeonho = intent.getStringExtra("baljubeonho").toString()


        setupEvents()
        setValues()

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

        binding.btnSave.setOnClickListener {
            saveDialog()
        }


        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = ""
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

    override fun setValues() {

        mAdapter = OrderDetailListAdapter(this, mContext, baljuDetail)
        binding.recyclerView.adapter = mAdapter


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
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                        binding.spinnerWareHouse.setSelection(0, false)
                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                        binding.spinnerWareHouse.setSelection(0, false)
                    }
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

        mAdapter.notifyDataSetChanged()

    }

    fun getRequestOrderDetail() {

        val savedOrderDetailList = mSqliteDB.getSavedOrderDetail()
        if (savedOrderDetailList.size > 0 && savedOrderDetailList[0].baljubeonho == mBaljubeonho) {
            orderDetailData = savedOrderDetailList[0]
            baljuDetail.clear()
            baljuDetail.addAll(orderDetailData.returnBaljudetail())


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

        mSqliteDB.insertOrderDetail(orderDetailData)

    }

    override fun onClickedEdit(count: Int, data: Baljudetail) {

        Log.d("yj", "Count : $count")

        dialog.setCount(mBaljubeonho, count, data)
        dialog.show(supportFragmentManager, "EditDialog")

    }

}