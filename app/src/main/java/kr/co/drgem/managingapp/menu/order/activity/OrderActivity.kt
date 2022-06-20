/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : OrderActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 매입입고 화면으로 발주번호요청 기능
 */

package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
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
    val loadingDialog = LoadingDialogFragment()

    lateinit var masterData: MasterDataResponse
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

        masterData = intent.getSerializableExtra("masterData") as MasterDataResponse


        setupEvents()
        setValues()
        getAllBaljubeonhoInLocalDB()
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
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
            getRequestOrderNum()
        }

    }

    //    발주번호요청
    fun getRequestOrderNum() {

        loadingDialog.show(supportFragmentManager, null)

        val georaecheomyeong = binding.edtGeoraecheomyeong.text.toString()
        val baljubeonho = binding.edtBaljubeonho.text.toString()

        apiList.getRequestOrderNumber(
            "02011",
            calStartStr,
            calEndStr,
            georaecheomyeong,
            baljubeonho
        )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    response.body()?.let {

                        baljuList.clear()
                        baljuList.addAll(it.returnBaljubeonho())

                        if (baljuList.size == 0) {
                            Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                            mOrderAdapter.clearList()
                        }

                        setBaljubeonhoListData()

                        clearDbAndInsertAllSearchedData()


                    }
                    loadingDialog.dismiss()

                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(mContext, "${t.message}", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                }

            })
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

            calStart.time = dateFormat.parse(data.BALJUILJASTART)
            calEnd.time = dateFormat.parse(data.BALJUILJAEND)

            calStartStr = dateSet.format(calStart.time)
            calEndStr = dateSet.format(calEnd.time)

        }

        baljuList.clear()
        baljuList.addAll(mSqliteDB.getAllSavedBaljubeonho())

        setBaljubeonhoListData()
    }

    override fun setValues() {

        mOrderAdapter = OrderListAdapter(masterData, baljuList)
        binding.recyclerView.adapter = mOrderAdapter

    }

    fun setBaljubeonhoListData() {

        if (baljuList.size > 0) {
            binding.layoutList.isVisible = true
            binding.layoutEmpty.isVisible = false
        }


        binding.txtCount.text = "(${baljuList.size}건)"

        mOrderAdapter.notifyDataSetChanged()

    }


}