package kr.co.drgem.managingapp.menu.transaction.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityTransactionBinding
import kr.co.drgem.managingapp.menu.transaction.adapter.TransactionAdapter
import kr.co.drgem.managingapp.menu.transaction.dialog.TransactionDialog
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionActivity : BaseActivity(), transactionEditListener {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter: TransactionAdapter
    lateinit var detailCode: Detailcode

    var georaedetail = ArrayList<GeoraedetailAdd>()
    var mWareHouseList : ArrayList<Detailcode> = arrayListOf()
    var companyCode = "0001"
    var wareHouseCode = "1001"

    lateinit  var tranData : TranResponse

    val dialogEdit = TransactionDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)


        setupEvents()

        getRequestTran()

        sort()

    }

    override fun setupEvents() {


        binding.layoutBigo.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("비고 내용 전체")
                .setMessage(tranData.getBigoHP())
                .setNegativeButton("확인", null)
                .show()
        }



        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = dateServer.format(cal.time)
        binding.layoutDate.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calDate = dateServer.format(cal.time)
                    binding.txtDate.text = dateFormat.format(cal.time)

                }
            }

            val datePick = DatePickerDialog(
                mContext,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()

        }


        binding.btnBack.setOnClickListener {

            backDialog()

        }

        binding.btnFold.setOnClickListener {
            binding.layoutFold.isVisible = false
            binding.btnOpen.isVisible = true
        }

        binding.btnOpen.setOnClickListener {
            binding.layoutFold.isVisible = true
            binding.btnOpen.isVisible = false
        }

        binding.btnTranRemove.setOnClickListener {
            binding.edtTranNum.text = null
            binding.layoutInfo.isVisible = false
        }

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }


        binding.btnSave.setOnClickListener {
            saveDialog()

            val inputName = binding.edtName.text.toString()


            val georaeMap = hashMapOf(
                "requesttype" to "02002",
                "georaemyeongsebeonho" to "X",
                "georaecheocode" to tranData.georaecheocode,
                "ipgoilja" to calDate,
                "ipgosaupjangcode" to companyCode,
                "ipgochanggocode" to wareHouseCode,
                "ipgodamdangja" to inputName,
                "pummokcount" to "",
                "georaedetail" to georaedetail
                )

            Log.d("yj", "맵확인 : $georaeMap")

        }

    }

    override fun setValues() {

        binding.georaecheomyeong.text = tranData.getGeoraecheomyeongHP()
        binding.nappumcheomyeong.text = tranData.getNappumcheomyeongHP()
        binding.bigo.text = tranData.getBigoHP()
        binding.txtCount.text = "(${tranData.returnGeoraedetail().size}건)"



        tranData.returnGeoraedetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.serialDetail.isVisible = true
            }
        }

        mAdapter = TransactionAdapter(this)
        binding.recyclerView.adapter = mAdapter



        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse

        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(
                mContext,
                R.layout.spinner_list_item,
                masterData.getCompanyCode()
            )
        binding.spinnerCompany.adapter = spinnerCompanyAdapter


        val spinnerWareHouseAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, arrayListOf())
        binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

        binding.spinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                        companyCode = "0001"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGwangmyeongCode())

                        if(mWareHouseList.size > 0) {
                            wareHouseCode = mWareHouseList[0].code
                        }

                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                        companyCode = "0002"

                        mWareHouseList.clear()
                        mWareHouseList.addAll(masterData.getGumiCode())

                        if(mWareHouseList.size > 0) {
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

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


    }

    fun getRequestTran(){

        binding.btnFind.setOnClickListener {

            val inputNum = binding.edtTranNum.text.toString()

            apiList.getRequestTranDetail("02001",inputNum).enqueue(object : Callback<TranResponse>{
                override fun onResponse(
                    call: Call<TranResponse>,
                    response: Response<TranResponse>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            tranData = it


                            if(it.returnGeoraedetail().size == 0){
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {

                                setValues()
                                mAdapter.setList(it.returnGeoraedetail())

                                binding.layoutEmpty.isVisible = false
                                binding.layoutList.isVisible = true
                                binding.layoutInfo.isVisible = true
                                binding.layoutFold.isVisible = true
                                binding.btnSave.isVisible = true

                            }

                        }
                    }
                }

                override fun onFailure(call: Call<TranResponse>, t: Throwable) {
                    Log.d("yj", "거래명세요청실패 : ${t.message}" )
                }

            })
        }
    }


    fun sort() {

        var onClickSeq = 0

        binding.layoutSeq.setOnClickListener {

            if (onClickSeq < 2) {
                onClickSeq++
            } else {
                onClickSeq = 0
            }

            when (onClickSeq) {

                0 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(tranData.returnGeoraedetail())

                }

                1 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(tranData.getDownSeq())

                }

                2 -> {
                    binding.imgSeq.setImageResource(R.drawable.dropup)
                    mAdapter.setList(tranData.getUpSeq())
                }
            }

        }

        var onClickLocation = 0

        binding.layoutLocation.setOnClickListener {

            if (onClickLocation < 2) {
                onClickLocation++
            } else {
                onClickLocation = 0
            }

            when (onClickLocation) {

                0 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropempty)
                    mAdapter.setList(tranData.returnGeoraedetail())
                }

                1 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropdown)
                    mAdapter.setList(tranData.getDownLocation())
                }

                2 -> {
                    binding.imgLocation.setImageResource(R.drawable.dropup)
                    mAdapter.setList(tranData.getUpLocation())
                }
            }

        }
    }


    override fun onClickedEdit(count : Int, data : Georaedetail) {
        dialogEdit.show(supportFragmentManager, "dialog")
        dialogEdit.setCount(count, data)

    }


    override fun onBackPressed() {

        backDialog()

    }


}