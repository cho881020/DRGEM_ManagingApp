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

    lateinit var binding   : ActivityMenuBinding
    lateinit var masterData: MasterDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        // 사업장.창고 마스터 데이터 가져오고  사원마스터 가져오고
        // 복구데이터 존재여부 검사 및 그에 따른 이후 복구작업 지시
        getRequestMasterCodeAndRecovery()

        setupEvents()  // 클릭 이벤트 설정 및 그에 따른 작업 지시
        setValues()    // 단순히 사용자명 디스플레이
    }

    override fun setupEvents() {

        binding.transaction.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("01")  // 거래명세입고
            val myIntent = Intent(this, TransactionActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.orderReceipt.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("02")  // 매입입고
            val myIntent = Intent(this, OrderActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.kitting.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("03")  // 키팅출고
            val myIntent = Intent(this, KittingActivity::class.java)
            startActivity(myIntent)
        }

        binding.request.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("04")  // 요청출고
            val myIntent = Intent(this, RequestActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.notDelivery.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("05")  // 미출자재출고
            val myIntent = Intent(this, NotDeliveryActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            startActivity(myIntent)
        }

        binding.location.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("06")  // 로케이션조회
            val myIntent = Intent(this, LocationActivity::class.java)
            startActivity(myIntent)
        }

        binding.stock.setOnClickListener {
            mSqliteDB.updateLoginWorkCommonWorkGubun("07")  // 재고조사
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

    // 단순히 사용자명 디스플레이
    override fun setValues() {
        val userName = mSqliteDB.getAllLoginWorkCommon()[0].USERNAME
        binding.sawonmyeong.text = "$userName 님"
    }

    fun getRequestMasterCodeAndRecovery() {

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
                        SawonDataManager.setSawonData(it)
                        checkRecovery()
                    }
                }
            }
            override fun onFailure(call: Call<MasterSawonResponse>, t: Throwable) {
            }
        })
    }

    // 복구작업으로 진행해야하는 지 검사 및 진행
    fun checkRecovery() {

        if (intent.getBooleanExtra("WorkRecovery", false)) { // 복구 작업 진행

            // workType에 해당되는 작업을 진행한다.
            //val workType = intent.getStringExtra("workType")
            val workType = mSqliteDB.getAllLoginWorkCommon()[0].WORKGUBUN.toString()
            workType.let {
                when (workType) {
                    "01" -> {  // 거래명세입고
//                        val myIntent = Intent(this, TransactionActivity::class.java)
//                        myIntent.putExtra("masterData", masterData)
//                        myIntent.putExtra("WorkRecovery"  , true)
//                        startActivity(myIntent)
                    }
                    "02" -> {  // 매입입고
                        val myIntent = Intent(mContext, OrderActivity::class.java)
                        myIntent.putExtra("masterData", masterData)  // 사업장.창고 마스터
                        // 로그인과 동일하게 사용- 다음화면에서 복구로 진행되도록 한다.
                        myIntent.putExtra("WorkRecovery"  , true)

                        // 아래의 문장도 업무에서 조회하면 나타나는 것이므로 일단 삭제
                        // myIntent.putExtra("lastWorkSEQ",
                        //    mSqliteDB.getAllLoginWorkCommon()[0].WORKNUMBER)
                        startActivity(myIntent)
                    }
                    "03" -> {  // 키팅출고
                    }
                    "04" -> {  // 요청출고
                    }
                    "05" -> {  // 미출자재출고
                    }
                    "06" -> {  // 로케이션조회
                    }
                    "07" -> {  // 재고조사
                    }
                }
            }
        } else {
            // 별도의 처리작업 없다. 통상의 작업 진행
        }
    }
}