package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderBinding
import kr.co.drgem.managingapp.menu.order.adapter.OrderListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog
import kr.co.drgem.managingapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity : BaseActivity() {

    lateinit var binding : ActivityOrderBinding
    lateinit var mOrderAdapter : OrderListAdapter
    val baljuList = ArrayList<Baljubeonho>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        setupEvents()
        setValues()

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

        var calStart = ""
        var calEnd = ""

        binding.layoutDateStart.setOnClickListener {

            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

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
            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

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

        val georaecheomyeong = binding.edtGeoraecheomyeong.text.toString()
        val baljubeonho = binding.edtBaljubeonho.text.toString()


        binding.btnFind.setOnClickListener {
            apiList.getRequestOrderNumber("02011","20150310","20150310",georaecheomyeong,baljubeonho).enqueue(object : Callback<OrderResponse>{
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {

                    Log.d("yj", "콜확인 ${call.request().body()}")
                    Log.d("yj", "발주목록 성공 : ${response.body()?.baljubeonho}")

                        response.body()?.let {
                            baljuList.clear()
                            baljuList.addAll(it.baljubeonho)
                            mOrderAdapter.notifyDataSetChanged()
                            Log.d("yj", "발주목록 성공 : ${it.resultmsg}")
//                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Log.d("yj", "발주 오류 : ${t.message}")
                }

            })
        }

        binding.layoutList.isVisible = true
        binding.layoutEmpty.isVisible = false

    }

    override fun setValues() {

        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        mOrderAdapter = OrderListAdapter(baljuList, masterData)
        binding.recyclerView.adapter = mOrderAdapter




    }

}