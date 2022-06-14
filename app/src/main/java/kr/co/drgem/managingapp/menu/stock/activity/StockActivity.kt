package kr.co.drgem.managingapp.menu.stock.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityStockBinding
import kr.co.drgem.managingapp.menu.stock.adapter.StockListAdapter
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import kr.co.drgem.managingapp.utils.MainDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockActivity : BaseActivity() {

    lateinit var binding: ActivityStockBinding
    lateinit var mAdapter: StockListAdapter

    lateinit var productData: ProductInfoResponse
    var inputCode = ""

    var companyCode = "0002"
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()

    var SEQ = ""

    val mList: ArrayList<Pummokdetail> = arrayListOf()  // 리스트 추가시 화면에 보일 목록
    var addList: ArrayList<Pummokdetail> = arrayListOf()   //mList 에 담을 목록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

        setupEvents()
        spinnerSet()
    }

    override fun onBackPressed() {
        backDialog(null)
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
        }

        binding.btnSave.setOnClickListener {
            saveDialog(){
                postRequestStock()
            }
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }


        binding.btnFind.setOnClickListener {

            getRequestStock()

        }

        binding.btnReady.setOnClickListener {
            requestWorkseq()
        }

        binding.btnAdd.setOnClickListener {

            addList[0].hyeonjaegosuryang =
                binding.hyeonjaegosuryang.text.toString()    // 품목리스트는 배열 0개로 고정

            mList.forEach {
                if (it.pummokcode == addList[0].pummokcode) {
                    Toast.makeText(mContext, "이미 작성 된 품목입니다.", Toast.LENGTH_SHORT).show()

                    binding.layoutAdd.isVisible = false
                    binding.layoutFind.isVisible = true
                    return@setOnClickListener

                }
            }

            mList.addAll(addList)
            addList.clear()

            Log.d("yj", "mList : $mList")
            setValues()

            binding.layoutAdd.isVisible = false
            binding.layoutFind.isVisible = true

        }

    }

    override fun setValues() {
        mAdapter = StockListAdapter(mList)
        binding.recyclerView.adapter = mAdapter

    }

    fun setText() {
        binding.pummokcode.text = (inputCode)
        binding.pummyeong.text = productData.returnPummokDetail()[0].getPummokcodeHP()
        binding.dobeonModel.text = productData.returnPummokDetail()[0].getdobeon_modelHP()
        binding.sayang.text = productData.returnPummokDetail()[0].getsayangHP()
        binding.hyeonjaegosuryang.setText(productData.returnPummokDetail()[0].gethyeonjaegosuryangHP())
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

    fun requestWorkseq() {
        var sawonCode = ""
        LoginUserUtil.getLoginData()?.let {
            sawonCode = it.sawoncode.toString()
        }

        // TODO - API 정상 연동시 수정
        val SEQMap = hashMapOf(
            "requesttype" to "",
            "pid" to "01",
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

    fun getRequestStock(){
        inputCode = binding.edtCode.text.toString()
        if (inputCode.isEmpty()) {
            Toast.makeText(mContext, "요청코드를 입력하세요", Toast.LENGTH_SHORT).show()
        }
        else{
            apiList.getRequestProductinfo("02091", inputCode, companyCode, wareHouseCode)
                .enqueue(object : Callback<ProductInfoResponse> {
                    override fun onResponse(
                        call: Call<ProductInfoResponse>,
                        response: Response<ProductInfoResponse>
                    ) {
                        if (response.isSuccessful) {

                            response.body()?.let {


                                if (it.returnPummokDetail().size == 0) {
                                    Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT)
                                        .show()

                                } else {
                                    productData = it
                                    addList.clear()
                                    addList.addAll(it.returnPummokDetail())
                                    Log.d("yj", "addList : $addList")

                                    binding.layoutEmpty.isVisible = false
                                    binding.layoutFind.isVisible = false
                                    binding.layoutAdd.isVisible = true
                                    binding.layoutList.isVisible = true

                                    setText()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ProductInfoResponse>, t: Throwable) {

                    }
                })
        }

    }

    fun postRequestStock() {

        val stockAddList: ArrayList<StockPummokdetail> = arrayListOf()

        mList.forEach {
            var pummokcode = ""
            var suryang = ""

            pummokcode = it.getPummokcodeHP()
            suryang = it.gethyeonjaegosuryangHP()

            stockAddList.add(StockPummokdetail(pummokcode, suryang))
        }

        val stockAdd = StockAdd(
            "02092",
            companyCode,    // TODO : 창고코드/로케이션 확인
            wareHouseCode,
            SEQ,
            "777",
            mList.size.toString(),
            stockAddList
        )

        Log.d("yj", "재고등록맵확인 : $stockAdd")

        apiList.postRequestStock(stockAdd).enqueue(object : Callback<WorkResponse>{
            override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.resultcd == "000") {


                            Toast.makeText(mContext, "저장이 완료되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            mList.clear()
                            setValues()
                        } else {
                            Toast.makeText(mContext, it.resultmsg, Toast.LENGTH_SHORT)
                                .show()
                        }

                        Log.d("yj", "재고등록 콜 결과코드 : ${it.resultcd}")
                        Log.d("yj", "재고등록 콜 결과메시지 : ${it.resultmsg}")
                    }
                }
            }

            override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                Log.d("yj", "재고등록 실패 결과메시지 : ${t.message}")
            }

        })

    }

}
