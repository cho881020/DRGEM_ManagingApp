package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderBinding
import kr.co.drgem.managingapp.menu.order.adapter.OrderListAdapter
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.MasterDataResponse
import kr.co.drgem.managingapp.models.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : BaseActivity() {

    lateinit var binding: ActivityOrderBinding
    lateinit var mOrderAdapter: OrderListAdapter
    val baljuList = ArrayList<Baljubeonho>()


    val calStart = Calendar.getInstance()
    val calEnd = Calendar.getInstance()
    val dateSet = SimpleDateFormat("yyyyMMdd")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    var calStartStr = dateSet.format(calStart.time)
    var calEndStr = dateSet.format(calEnd.time)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        setupEvents()
        setValues()

        getAllBaljubeonhoInLocalDB()
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.btnCompanyRemove.setOnClickListener {
            binding.edtGeoraecheomyeong.text = null
        }

        binding.btnOrderRemove.setOnClickListener {
            binding.edtBaljubeonho.text = null
        }


        binding.txtDateStart.text = dateFormat.format(calStart.time)
        binding.txtDateEnd.text = dateFormat.format(calEnd.time)


        binding.layoutDateStart.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    calStart.set(year, month, dayOfMonth)

                    calStartStr = dateSet.format(calStart.time)
                    binding.txtDateStart.text = dateFormat.format(calStart.time)

                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                calStart.get(Calendar.YEAR),
                calStart.get(Calendar.MONTH),
                calStart.get(Calendar.DAY_OF_MONTH)
            )
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()

        }

        binding.layoutDateEnd.setOnClickListener {
            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    calEnd.set(year, month, dayOfMonth)

                    calEndStr = dateSet.format(calEnd.time)
                    binding.txtDateEnd.text = dateFormat.format(calEnd.time)
                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                calEnd.get(Calendar.YEAR),
                calEnd.get(Calendar.MONTH),
                calEnd.get(Calendar.DAY_OF_MONTH)
            )
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()
        }


        binding.btnFind.setOnClickListener {

            val georaecheomyeong = binding.edtGeoraecheomyeong.text.toString()
            val baljubeonho = binding.edtBaljubeonho.text.toString()

            apiList.getRequestOrderNumber("02011", calStartStr, calEndStr, georaecheomyeong, baljubeonho)
                .enqueue(object : Callback<OrderResponse> {
                    override fun onResponse(
                        call: Call<OrderResponse>,
                        response: Response<OrderResponse>
                    ) {
                        Log.d("yj", "cal:$calStartStr $calEndStr, 거래처명:$georaecheomyeong, 발주번호:$baljubeonho" )

                            response.body()?.let {

                                baljuList.clear()
                                baljuList.addAll(it.returnBaljubeonho())


                                if(baljuList.size == 0){
                                    Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                                }

                                setBaljubeonhoListData()

                                clearDbAndInsertAllSearchedData()



                            }



                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        Log.d("yj", "발주 번호 오류 : ${t.message}")
                    }

                })
        }



    }

    private fun clearDbAndInsertAllSearchedData() {

        mSqliteDB.deleteBaljuCommon()
        mSqliteDB.insertBaljuCommon(
            dateFormat.format(calStart.time),
            dateFormat.format(calEnd.time),
            binding.edtGeoraecheomyeong.text.toString(),
            binding.edtBaljubeonho.text.toString()
        )

        mSqliteDB.deleteBaljubeonho()
        for (data in baljuList) {

            mSqliteDB.insertBaljubeonho(data)

        }
    }

    private fun getAllBaljubeonhoInLocalDB() {

        val baljuCommonDataList = mSqliteDB.getAllBaljuCommon()

        if (baljuCommonDataList.isNotEmpty()) {

            val data = baljuCommonDataList[0]

            binding.txtDateStart.text = data.BALJUILJASTART
            binding.txtDateEnd.text = data.BALJUILJAEND
            binding.edtGeoraecheomyeong.setText(data.GEORAECHEOMEONG)
            binding.edtBaljubeonho.setText(data.BALJUBEONHO)

        }

        baljuList.clear()
        baljuList.addAll(mSqliteDB.getAllSavedBaljubeonho())

        setBaljubeonhoListData()
    }

    override fun setValues() {

        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        mOrderAdapter = OrderListAdapter(baljuList, masterData)
        binding.recyclerView.adapter = mOrderAdapter



    }

    fun setBaljubeonhoListData() {

        if(baljuList.size > 0){
            binding.layoutList.isVisible = true
            binding.layoutEmpty.isVisible = false
        }

        binding.txtCount.text = "(${baljuList.size}건)"


        mOrderAdapter.notifyDataSetChanged()

    }


}