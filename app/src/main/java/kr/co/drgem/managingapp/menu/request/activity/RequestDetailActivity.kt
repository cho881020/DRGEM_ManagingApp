package kr.co.drgem.managingapp.menu.request.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityRequestDetailBinding
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.adapter.RequestDetailListAdapter
import kr.co.drgem.managingapp.menu.request.dialog.RequestDetailDialog
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.KittingDetailResponse
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.RequestDetailResponse
import kr.co.drgem.managingapp.utils.MainDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestDetailActivity : BaseActivity(), RequestDetailEditListener {

    lateinit var binding: ActivityRequestDetailBinding
    lateinit var mAdapter: RequestDetailListAdapter
    lateinit var requestDetailData: RequestDetailResponse

    val dialog = RequestDetailDialog()

    lateinit var mYocheongbeonho: String

    var johoejogeon = "0"
    var migwanri = "0"

    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var companyCode = "0001"
    var wareHouseCode = "1001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail)

        getRequestDetail()
        spinnerSet()
        getRequestJohoejogeon()
        setupEvents()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun setValues() {

        mAdapter = RequestDetailListAdapter(requestDetailData.returnPummokDetail(),this)
        binding.recyclerView.adapter = mAdapter

        binding.yocheongbeonho.text = "요청번호 - $mYocheongbeonho"
        binding.yocheongbeonho2.text = mYocheongbeonho
        binding.txtCount.text = "(${requestDetailData.pummokcount}건)"

        requestDetailData.returnPummokDetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.jungyojajeyeobu.isVisible = true
                binding.serialDetail.isVisible = true
            }
        }
    }


    fun getRequestJohoejogeon() {

        binding.radio0.setOnClickListener {
            johoejogeon = "0"
            getRequestDetail()
        }

        binding.radio1.setOnClickListener {
            johoejogeon = "1"
            getRequestDetail()
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                migwanri = "0"
            } else {
                migwanri = "1"
            }
            getRequestJohoejogeon()
        }

    }

    fun spinnerSet() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, it.getCompanyCode())
            binding.spinnerCompany.adapter = spinnerCompanyAdapter


            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
            binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter


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

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }

                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCode = "0002"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGumiCode())

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }

                        }
                        Log.d("yj", "companyCode : $companyCode")
                        Log.d("yj", "waarHouseCode : $wareHouseCode")
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
                        Log.d("yj", "waarHouseCode : $wareHouseCode")
                        getRequestDetail()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }


    fun getRequestDetail() {
        mYocheongbeonho = intent.getStringExtra("yocheongbeonho").toString()

        apiList.getRequestRequestDetail( "02062", mYocheongbeonho, johoejogeon, migwanri, companyCode, wareHouseCode).enqueue(object :Callback<RequestDetailResponse>{
            override fun onResponse(
                call: Call<RequestDetailResponse>,
                response: Response<RequestDetailResponse>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        requestDetailData = it

                        setValues()

                    }
                }
            }

            override fun onFailure(call: Call<RequestDetailResponse>, t: Throwable) {

            }

        })

    }


    override fun onClickedEdit(count: Int, data: Pummokdetail) {
        dialog.setCount(mYocheongbeonho, count, data)
        dialog.show(supportFragmentManager, "dialog_request")
    }


}