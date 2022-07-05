/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : MainActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 메뉴 선택 화면으로 공통 마스터데이터 요청 및 화면 전환 기능
 */

package kr.co.drgem.managingapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMenuBinding
import kr.co.drgem.managingapp.menu.kitting.activity.KittingActivity
import kr.co.drgem.managingapp.menu.location.activity.LocationActivity
import kr.co.drgem.managingapp.menu.notdelivery.activity.NotDeliveryActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderActivity
import kr.co.drgem.managingapp.menu.request.activity.RequestActivity
import kr.co.drgem.managingapp.menu.stock.activity.StockActivity
import kr.co.drgem.managingapp.menu.transaction.activity.TransactionActivity
import kr.co.drgem.managingapp.models.MasterDataResponse
import kr.co.drgem.managingapp.models.MasterSawonResponse
import kr.co.drgem.managingapp.utils.ContextUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SawonDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : BaseActivity() {

    lateinit var binding: ActivityMenuBinding
    lateinit var masterData: MasterDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        getRequestMasterCode()
//        마스터 끝나고 => 사원코드 끝나고 => 복구모두로 넘어갈지 체크

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.transaction.setOnClickListener {
            val myIntent = Intent(this, TransactionActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.orderReceipt.setOnClickListener {
            val myIntent = Intent(this, OrderActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.kitting.setOnClickListener {
            val myIntent = Intent(this, KittingActivity::class.java)
            startActivity(myIntent)
        }

        binding.request.setOnClickListener {
            val myIntent = Intent(this, RequestActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.notDelivery.setOnClickListener {
            val myIntent = Intent(this, NotDeliveryActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.location.setOnClickListener {
            val myIntent = Intent(this, LocationActivity::class.java)
            startActivity(myIntent)
        }

        binding.stock.setOnClickListener {
            val myIntent = Intent(this, StockActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setNeutralButton("예", DialogInterface.OnClickListener { dialog, which ->
                    ContextUtil.setToken(mContext, "")
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                    finish()
                })
                .setNegativeButton("아니오", null)
                .show()
        }

    }

    override fun onBackPressed() {
        endDialog()
    }

    override fun setValues() {

        val userName = mSqliteDB.getAllLoginWorkCommon()[0].USERNAME
        binding.sawonmyeong.text = "$userName 님"

        


    }

    fun getRequestMasterCode() {

        apiList.getRequestMasterData().enqueue(object : Callback<MasterDataResponse> {
            override fun onResponse(
                call: Call<MasterDataResponse>,
                response: Response<MasterDataResponse>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        masterData = it
                        MainDataManager.setMainData(it)
                        Log.d("yj", "masterDataResponse : $it")

                        getRequestMasterSawon()
                    }
                }
            }

            override fun onFailure(call: Call<MasterDataResponse>, t: Throwable) {
                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                Log.d("yj", "MasterCode 실패 : ${t.message}")
            }

        })
    }

    fun getRequestMasterSawon(){
        apiList.getRequestMasterSawon().enqueue(object : Callback<MasterSawonResponse>{
            override fun onResponse(
                call: Call<MasterSawonResponse>,
                response: Response<MasterSawonResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let{
                        SawonDataManager.setSawonDataList(it)
                        checkRecovery()
                    }
                }
            }

            override fun onFailure(call: Call<MasterSawonResponse>, t: Throwable) {

            }


        })
    }

    fun checkRecovery() {
        val workType = intent.getStringExtra("workType")
        workType?.let {
            when (workType) {
                "02" -> {
                    val myIntent = Intent(mContext, OrderActivity::class.java)
                    myIntent.putExtra("masterData", masterData)
                    myIntent.putExtra("lastWorkSEQ", mSqliteDB.getAllLoginWorkCommon()[0].WORKNUMBER)
                    startActivity(myIntent)
                }
            }

        }

    }

}