/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : StockActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 재고조사 화면으로 품목정보요청 및 재고수량등록 기능
 */


package kr.co.drgem.managingapp.menu.stock.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonArray
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityStockBinding
import kr.co.drgem.managingapp.menu.stock.adapter.StockListAdapter
import kr.co.drgem.managingapp.menu.stock.dialog.LoadingStockDialogFragment
import kr.co.drgem.managingapp.menu.stock.dialog.StockDialogFragment
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class StockActivity : BaseActivity(), DialogInterface.OnDismissListener {

    lateinit var binding: ActivityStockBinding
    lateinit var mAdapter: StockListAdapter

    lateinit var productData: ProductInfoResponse
    var inputCode = ""

    var companyCode = "0002"        // 조회코드
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()

    var saeopjangcode = ""          // 등록코드
    var changgocode = ""

    val loadingDialog = LoadingStockDialogFragment()
    val stockCodeDialog = StockDialogFragment()

    var SEQ = ""
    var status = "111"
    var sawonCode = ""

    val mList: ArrayList<Pummokdetail> = arrayListOf()  // 리스트 추가시 화면에 보일 목록
    lateinit var searchCodeData: Pummokdetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

        setupEvents()
        spinnerSet()
    }

    override fun onBackPressed() {
        backDialog() {
            workStatusCancle()
        }
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog() {
                workStatusCancle()
            }
        }

        binding.btnSave.setOnClickListener {
            stockCodeDialog.show(supportFragmentManager, null)
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }

        binding.btnReady.setOnClickListener {
            requestWorkseq()
        }

        searchStock()


        binding.btnAdd.setOnClickListener {

            val cal = Calendar.getInstance()
            val dateServer = SimpleDateFormat("yyyyMMddhhmmss")  // 서버 전달 포맷
            val josasigan = dateServer.format(cal.time)

            searchCodeData.setJosasiganAdd(josasigan)
            searchCodeData.hyeonjaegosuryang =
                binding.suryang.text.toString()

            searchCodeData.setLocationAdd(binding.locationAdd.text.toString())


            if (searchCodeData.gethyeonjaegosuryangHP() == "") {
                AlertDialog.Builder(mContext)
                    .setMessage("수량을 입력 해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()
                return@setOnClickListener
            }


            mList.forEach {
                if (it.pummokcode == searchCodeData.pummokcode) {

                    AlertDialog.Builder(mContext)
                        .setMessage("이미 작성 된 품목입니다.")
                        .setNegativeButton("확인", null)
                        .show()

                    binding.layoutAdd.isVisible = false
                    binding.layoutFind.isVisible = true
                    return@setOnClickListener

                }
            }

            mList.addAll(listOf(searchCodeData))

            Log.d("yj", "mList : $mList")
            setValues()

            binding.layoutAdd.isVisible = false
            binding.layoutFind.isVisible = true

            binding.suryang.setText("0")
            binding.locationAdd.setText("")


        }

    }

    override fun setValues() {
        mAdapter = StockListAdapter(mList)
        binding.recyclerView.adapter = mAdapter

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

    //    작업 SEQ 요청
    fun requestWorkseq() {

        loadingDialog.show(supportFragmentManager, null)

        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        val SEQMap = hashMapOf(
            "requesttype" to "08001",
            "pid" to "07",
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

                            getRequestStock()

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

    //    품목정보요청
    fun getRequestStock() {

        apiList.getRequestProductinfo("02091", "", companyCode, wareHouseCode)
            .enqueue(object : Callback<ProductInfoResponse> {
                override fun onResponse(
                    call: Call<ProductInfoResponse>,
                    response: Response<ProductInfoResponse>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let {


                            if (it.returnPummokDetail().size == 0) {
                                searchZeroDialog()
                                status = "111"

                            } else {


                                status = "333"
                                loadingDialog.loadingEnd()

                                productData = it

                                binding.layoutEmpty.isVisible = false
                                binding.layoutReady.isVisible = false
                                binding.layoutAdd.isVisible = false
                                binding.layoutFind.isVisible = true
                                binding.layoutList.isVisible = true
                                binding.btnSave.isVisible = true

                            }
                        }


                    }
                }

                override fun onFailure(call: Call<ProductInfoResponse>, t: Throwable) {
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
                }
            })

    }

    //    재고수량등록
    fun postRequestStock() {

        val stockAddList = JsonArray()

        mList.forEach {
            var pummokcode = ""
            var suryang = ""

            pummokcode = it.getPummokcodeHP()
            suryang = it.gethyeonjaegosuryangHP()

            stockAddList.add(
                StockPummokdetail(
                    pummokcode,
                    suryang,
                    it.getJosasiganAdd(),
                    it.getLocationAdd()
                ).toJsonObject()
            )
        }

        val stockAdd = hashMapOf(
            "requesttype" to "02092",
            "seq" to SEQ,
            "tabletip" to IPUtil.getIpAddress(),
            "sawoncode" to sawonCode,
            "saeopjangcode" to saeopjangcode,
            "changgocode" to changgocode,
            "status" to "777",
            "pummokcount" to mList.size.toString(),
            "pummokdetail" to stockAddList
        )

        Log.d("yj", "재고등록맵확인 : $stockAdd")

        apiList.postRequestStock(stockAdd).enqueue(object : Callback<WorkResponse> {
            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.resultcd == "000") {

                            saveDoneDialog()

                            mList.clear()
                            setValues()

                            binding.layoutEmpty.isVisible = true
                            binding.layoutReady.isVisible = true
                            binding.layoutAdd.isVisible = false
                            binding.layoutFind.isVisible = false
                            binding.layoutList.isVisible = false
                            binding.btnSave.isVisible = false
                            binding.suryang.setText("0")
                            binding.locationAdd.setText("")


                        } else {
                            serverErrorDialog(it.resultmsg)
                        }

                        Log.d("yj", "재고등록 콜 결과코드 : ${it.resultcd}")
                        Log.d("yj", "재고등록 콜 결과메시지 : ${it.resultmsg}")
                    }
                }
            }

            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
            }

        })

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

    fun searchStock() {

        binding.btnFind.setOnClickListener {


            inputCode = binding.edtCode.text.toString()

            searchCodeData = Pummokdetail("", "", "", "", "", "", "", "", "", "", "", "")

            productData.returnPummokDetail().forEach {

                if (inputCode == it.pummokcode) {
                    searchCodeData = it

                    binding.pummokcode.text = (inputCode)
                    binding.pummyeong.text = searchCodeData.getPummokcodeHP()
                    binding.dobeonModel.text = searchCodeData.getdobeon_modelHP()
                    binding.sayang.text = searchCodeData.getsayangHP()
                    binding.suryang.setText(searchCodeData.gethyeonjaegosuryangHP())

                }

            }

            if (searchCodeData.pummokcode == "") {
                searchZeroDialog()
                return@setOnClickListener
            }

            binding.layoutAdd.isVisible = true
            binding.layoutFind.isVisible = false


        }


    }

    override fun onDismiss(p0: DialogInterface?) {


        saeopjangcode = stockCodeDialog.companyCode
        changgocode = stockCodeDialog.wareHouseCode
        Log.d("yj", "saeopjangcode : $saeopjangcode , changgocode : $changgocode")

        saveDialog() {
            postRequestStock()
        }
    }


}
