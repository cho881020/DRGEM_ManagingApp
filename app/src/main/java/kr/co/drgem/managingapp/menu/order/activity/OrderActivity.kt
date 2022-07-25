/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : OrderActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 매입입고 화면으로 발주번호요청 기능
 */

package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
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

    val loadingDialog = LoadingDialogFragment()

    lateinit var binding      : ActivityOrderBinding
    lateinit var mOrderAdapter: OrderListAdapter
    lateinit var masterData   : MasterDataResponse

    val baljuList   = ArrayList<Baljubeonho>()
    val calStart    = Calendar.getInstance()
    val calEnd      = Calendar.getInstance()
    val dateSet     = SimpleDateFormat("yyyyMMdd")
    val dateFormat  = SimpleDateFormat("yyyy-MM-dd")
    var calStartStr = dateSet.format(calStart.time)
    var calEndStr   = dateSet.format(calEnd.time)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding    = DataBindingUtil.setContentView(this, R.layout.activity_order)

        masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        setupEvents()
        setValues()  // 복구작업 존재하면 복구작업 진행까지
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

        // 검색버튼 클릭시
        binding.btnFind.setOnClickListener {

            // 발주번호 영문자 대문자로 변경하기
            val UpperCaseS = binding.edtBaljubeonho.text.toString().uppercase()
            binding.edtBaljubeonho.setText(UpperCaseS)

            getRequestOrderNum()  // 발주번호리스트 요청
        }
    }

    // 발주번호리스트 요청
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
                            searchZeroDialog()
                            mOrderAdapter.clearList()
                        }

                        setBaljubeonhoListData()          // 발주번호 갯수를 표시

                        clearDbAndInsertAllSearchedData() // 로컬테이블에 발주번호리스트 클리어 및 저장
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                }
            })
    }

    // 로컬테이블에 발주 공통정보 삭제 후 신규정보로 재 등록
    // 로컬테이블에 검색된 발주번호리스트 클리어 및 저장
    // 발주번호리스트 요청이 정상적으로 이루어지면 진행된다.
    private fun clearDbAndInsertAllSearchedData() {

// 테이블 통합전의 실행문 - 아래의 문장들로 대체 됨
//        mSqliteDB.deleteBaljuCommon()
//        mSqliteDB.insertBaljuCommon(    // 로컬테이블에 발주 검색 공통조건 저장
//            dateFormat.format(calStart.time),
//            dateFormat.format(calEnd.time),
//            binding.edtGeoraecheomyeong.text.toString(),
//            binding.edtBaljubeonho.text.toString()
//        )

        // 발주 공통정보 삭제 후 신규정보로 재 등록
        mSqliteDB.deleteBaljuInfoCommon()

        mSqliteDB.insertBaljuInfoCommon(    // 로컬테이블에 발주 검색 공통조건 저장
            dateFormat.format(calStart.time),
            dateFormat.format(calEnd.time),
            binding.edtGeoraecheomyeong.text.toString(),
            binding.edtBaljubeonho.text.toString(),
            // 이후의 항목들은 발주명세화면에서 발주명세정보 서버로부터 받은 후 업데이트 된다.
            // count 정보는 아직 미 처리 상태
           "",
           "",
           "",
           "",
           "",
           "",
           "",
           "",
            // 이후의 항목들은 발주명세화면에서 발주명세정보 서버로부터 받은 후와 임시저장시 업데이트 된다.
           "",
           "",
           "",
           ""
        )

        // 로컬테이블에 발주번호리스트 클리어 및 저장
        mSqliteDB.deleteBaljubeonho()
        for (data in baljuList) {

            mSqliteDB.insertBaljubeonho(data)
        }
    }

    // 복구작업 진행시 조회조건 및 데이터 셋업
    private fun getAllBaljubeonhoInLocalDB() {

        val baljuInfoCommonDataList = mSqliteDB.getAllBaljuInfoCommon()  // 발주검색정보 공통

        if (baljuInfoCommonDataList.isNotEmpty()) {

            val data = baljuInfoCommonDataList[0]

            binding.txtDateStart.text = data.BALJUILJASTART
            binding.txtDateEnd  .text = data.BALJUILJAEND
            binding.edtGeoraecheomyeong.setText(data.GEORAECHEOMEONG)
            binding.edtBaljubeonho     .setText(data.BALJUBEONHO)

            calStart.time = dateFormat.parse(data.BALJUILJASTART)
            calEnd  .time = dateFormat.parse(data.BALJUILJAEND)

            calStartStr = dateSet.format(calStart.time)
            calEndStr   = dateSet.format(calEnd.time)

        }

        baljuList.clear()
        baljuList.addAll(mSqliteDB.getAllSavedBaljubeonho())

        setBaljubeonhoListData()  // 발주번호 갯수를 표시
    }

    override fun setValues() {

        mOrderAdapter = OrderListAdapter(baljuList)
        binding.recyclerView.adapter = mOrderAdapter

        // 복구작업 진행
        //val lastWorkSEQ = intent.getStringExtra("lastWorkSEQ")
        //lastWorkSEQ?.let {
        if (intent.getBooleanExtra("WorkRecovery", false)) { // 복구작업 진행

            getAllBaljubeonhoInLocalDB()  // 복구작업 진행시 데이터 셋업

            //val orderData = mSqliteDB.getSavedOrderDetail()[0] // 발주번호를 넘겨주려고 하는 것 같은 데 // 공통 정보에 있어서 넘겨줄필요없다.
                                                                 // 이거 하나로 너무 많은 데이터를 읽어야 한다.
            val myIntent  = Intent(mContext, OrderDetailDetailActivity::class.java)

            //myIntent.putExtra("baljubeonho", orderData.baljubeonho)
            myIntent.putExtra("baljubeonho", mSqliteDB.getAllBaljuInfoCommon()[0].BALJUBEONHOSEL) // 발주번호는 이것으로 대체한다.
            myIntent.putExtra("seq"        , mSqliteDB.getAllLoginWorkCommon()[0].WORKNUMBER)
            myIntent.putExtra("WorkRecovery"  , true)
            startActivity(myIntent)
        }
    }

    // 발주번호 갯수를 표시
    fun setBaljubeonhoListData() {

        if (baljuList.size > 0) {
            binding.layoutList .isVisible = true
            binding.layoutEmpty.isVisible = false
        }

        binding.txtCount.text = "(${baljuList.size}건)"

        mOrderAdapter.notifyDataSetChanged()
    }
}