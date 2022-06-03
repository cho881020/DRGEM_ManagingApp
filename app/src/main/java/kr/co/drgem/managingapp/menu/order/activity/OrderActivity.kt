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
import kr.co.drgem.managingapp.roomdb.datas.BaljuRoomData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : BaseActivity() {

    lateinit var binding: ActivityOrderBinding
    lateinit var mOrderAdapter: OrderListAdapter
    val baljuList = ArrayList<Baljubeonho>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        setupEvents()
        setValues()

//        getAllBaljubeonhoListFromRoomDB()
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

        val cal = Calendar.getInstance()
        val dateSet = SimpleDateFormat("yyyyMMdd")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        binding.txtDateStart.text = dateFormat.format(cal.time)
        binding.txtDateEnd.text = dateFormat.format(cal.time)

        var calStart = dateSet.format(cal.time)
        var calEnd = dateSet.format(cal.time)

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
            ).show()

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
            ).show()

        }


        binding.btnFind.setOnClickListener {

            val georaecheomyeong = binding.edtGeoraecheomyeong.text.toString()
            val baljubeonho = binding.edtBaljubeonho.text.toString()

            apiList.getRequestOrderNumber("02011", calStart, calEnd, georaecheomyeong, baljubeonho)
                .enqueue(object : Callback<OrderResponse> {
                    override fun onResponse(
                        call: Call<OrderResponse>,
                        response: Response<OrderResponse>
                    ) {
                        Log.d("yj", "cal:$calStart $calEnd, 거래처명:$georaecheomyeong, 발주번호:$baljubeonho" )

                            response.body()?.let {

                                baljuList.clear()
                                baljuList.addAll(it.returnBaljubeonho())

                                if(baljuList.size == 0){
                                    Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                                }

                                setBaljubeonhoListData()

//                                clearDbAndInsertAllSearchedData()



                            }



                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        Log.d("yj", "발주 번호 오류 : ${t.message}")
                    }

                })
        }



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


//    private fun clearDbAndInsertAllSearchedData() {
//
//        roomDB.baljubeonhoDao().deleteAllSavedBaljubeonhoList()
//
//        roomDB.baljubeonhoDao().insertBaljubeonhoList(baljuList)
//
//        setBaljubeonhoListData()
//
//    }
//
//    fun getAllBaljubeonhoListFromRoomDB() {
//
//        val dbList = roomDB.baljubeonhoDao().getAllSavedBaljubeonhoList()
//
//        baljuList.clear()
//        baljuList.addAll(dbList)
//        setBaljubeonhoListData()
//
//    }

}