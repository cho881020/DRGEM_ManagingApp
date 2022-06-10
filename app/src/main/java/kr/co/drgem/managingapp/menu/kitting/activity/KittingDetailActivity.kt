package kr.co.drgem.managingapp.menu.kitting.activity

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
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.MainDataManager
import kr.co.drgem.managingapp.utils.SerialManageUtil
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener {

    lateinit var binding: ActivityKittingDetailBinding
    lateinit var mAdapter: KittingDetailListAdapter
    lateinit var kittingDetailData: KittingDetailResponse

    val dialog = KittingDetailDialog()

    lateinit var mkittingbeonho: String
    var johoejogeon = "0"
    var migwanri = "0"
    var changgocode = ""

    var companyCodeOut = "0001"
    var wareHouseCodeOut = "1001"
    var mWareHouseListOut: java.util.ArrayList<Detailcode> = arrayListOf()

    var companyCodeIn = "0001"
    var wareHouseCodeIn = "1001"
    var mWareHouseListIn: java.util.ArrayList<Detailcode> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting_detail)

        mkittingbeonho = intent.getStringExtra("kittingbeonho").toString()

        getRequestKittingDetail()
        setupEvents()
        getRequestJohoejogeon()
        spinnerSetOut()
        spinnerSetIn()
    }

    override fun onBackPressed() {
        backDialog(null)
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog(null)
        }

        binding.btnSave.setOnClickListener {
//            saveDialog(){
                postRequestKitting()
//            }
        }

        binding.btnOutNameRemove.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInNameRemove.setOnClickListener {
            binding.edtInName.text = null
        }

    }

    override fun setValues() {

        mAdapter = KittingDetailListAdapter(kittingDetailData.returnKittingDetail(), this)
        binding.recyclerView.adapter = mAdapter

        binding.kittingbeonho.text = "키팅번호 - $mkittingbeonho"
        binding.kittingbeonho2.text = mkittingbeonho
        binding.txtCount.text = "(${kittingDetailData.getPummokCount()} 건)"


        kittingDetailData.returnKittingDetail().forEach {
            if (it.jungyojajeyeobu == "Y") {
                binding.serialDetail.isVisible = true
            }
        }


    }

    fun getRequestJohoejogeon() {

        binding.radio0.setOnClickListener {
            johoejogeon = "0"
            getRequestKittingDetail()
        }

        binding.radio1.setOnClickListener {
            johoejogeon = "1"
            getRequestKittingDetail()
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                migwanri = "0"
            } else {
                migwanri = "1"
            }

            getRequestKittingDetail()
        }

        val changgoList = ArrayList<Detailcode>()
        changgoList.add(Detailcode("2001", "자재창고1"))
        changgoList.add(Detailcode("2014", "자재창고2"))

        val spinnerAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, changgoList)
        binding.spinnerChanggocode.adapter = spinnerAdapter

        binding.spinnerChanggocode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    changgocode = changgoList[position].code
                    Log.d("yj", "창고코드 : $changgocode")

                    getRequestKittingDetail()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


    }


    fun getRequestKittingDetail() {

        apiList.getRequestKittingDetail("02502", mkittingbeonho, johoejogeon, migwanri, changgocode)
            .enqueue(object : Callback<KittingDetailResponse> {
                override fun onResponse(
                    call: Call<KittingDetailResponse>,
                    response: Response<KittingDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            kittingDetailData = it

                            setValues()

                        }
                    }
                }

                override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                    Log.d("yj", "키팅명세요청실패 : ${t.message}")
                }

            })

    }

    fun postRequestKitting() {

        val kittingDetail = JSONArray()   // 등록용 리스트
        val chulgodamdangjacode = binding.edtOutName.text.toString()
        val ipgodamdangjacode = binding.edtInName.text.toString()


        kittingDetailData.returnKittingDetail().forEach {

            var serialData = SerialManageUtil.getSerialStringByPummokCode(it.getPummokcodeHP())
                .toString()      // 시리얼 데이터 꺼내오기

            if (serialData.isEmpty()) {        // 시리얼 데이터가 빈 값일 경우

                serialData = ""             // "" 으로 표시

            }

            if (serialData != "null") {

                kittingDetail.put(                         // 리스트에 담기
                    ChulgoDetailAdd(
                        "1",         //check : 요청번호?
                        it.getPummokcodeHP(),
                        serialData.split(",").size.toString(),
                        it.getjungyojajeyeobuHP(),
                        serialData
                    ).toJsonObject()                            // JSONObject로 제작
                )
            }

        }

        val chulgoMap = hashMapOf(
            "requesttype" to "02053",
            "kittingbeonho" to "000",
            "chulgoilja" to "20220510",
            "chulgosaupjangcode" to  companyCodeOut,
            "chulgochanggocode" to wareHouseCodeOut,
            "chulgodamdangjacode" to chulgodamdangjacode,
            "ipgosaupjangcode" to companyCodeIn,
            "ipgochanggocode" to wareHouseCodeIn,
            "ipgodamdangjacode" to ipgodamdangjacode,
            "seq" to "TEMP_SEQ", // TODO - SEQ 관련 API 연동 성공시 수정해야함
            "status" to "777",
            "pummokcount" to kittingDetail.length().toString(),
            "chulgodetail" to kittingDetail.toString()
        )


        Log.d("yj", "일괄출고등록 맵확인 : $chulgoMap")

        if (kittingDetail.length() > 0) {
            apiList.postRequestDeliveryBatch(chulgoMap).enqueue(object : Callback<WorkResponse>{
                override fun onResponse(
                    call: Call<WorkResponse>,
                    response: Response<WorkResponse>
                ) {


                    Log.d("yj", "일괄출고등록 콜 결과코드 : ${response.body()}")

                    response.body()?.let {
                        Log.d("yj", "일괄출고등록 콜 결과코드 : ${it.resultmsg}")
                    }



                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.resultcd == "000") {

                                SerialManageUtil.clearData()
                                mAdapter.notifyDataSetChanged()

                                Toast.makeText(mContext, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            }

                            Log.d("yj", "일괄출고등록 콜 결과코드 : ${it.resultcd}")
                            Log.d("yj", "일괄출고등록 콜 결과메시지 : ${it.resultmsg}")

                        }
                    }
                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {

                    Log.d("yj", "일괄출고등록 실패 결과메시지 : ${t.message}")
                }

            })


        } else {
            Toast.makeText(mContext, "저장할 자료가 없습니다.", Toast.LENGTH_SHORT).show()
        }


    }

    fun spinnerSetOut() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyOut.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseOut.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeOut = "0001"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)
                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeOut = "0002"

                            mWareHouseListOut.clear()
                            mWareHouseListOut.addAll(it.getGumiCode())
                            binding.spinnerWareHouseOut.setSelection(0, false)

                            if (mWareHouseListOut.size > 0) {
                                wareHouseCodeOut = mWareHouseListOut[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseOut.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeOut = mWareHouseListOut[position].code

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    fun spinnerSetIn() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompanyIn.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouseIn.adapter = spinnerWareHouseAdapter


            binding.spinnerCompanyIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCodeIn = "0001"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)
                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }


                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCodeIn = "0002"

                            mWareHouseListIn.clear()
                            mWareHouseListIn.addAll(it.getGumiCode())
                            binding.spinnerWareHouseIn.setSelection(0, false)

                            if (mWareHouseListIn.size > 0) {
                                wareHouseCodeIn = mWareHouseListIn[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

            binding.spinnerWareHouseIn.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCodeIn = mWareHouseListIn[position].code

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }

    override fun onClickedEdit(count: Int, data: Pummokdetail) {

        dialog.setCount(mkittingbeonho, count, data)
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }
}