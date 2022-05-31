package kr.co.drgem.managingapp.menu.transaction.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
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

class TransactionActivity : BaseActivity(), transactionEditListener {

    lateinit var binding: ActivityTransactionBinding
    lateinit var mAdapter: TransactionAdapter
    lateinit var detailCode: Detailcode

    lateinit  var tranData : TranResponse

    val dialogEdit = TransactionDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)


        setupEvents()


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

        binding.btnSave.setOnClickListener {
            Toast.makeText(mContext, "변경 사항이 저장 되었습니다", Toast.LENGTH_SHORT).show()
        }

        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = ""
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
            ).show()

        }


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
        }

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }


    }

    override fun setValues() {

        binding.georaecheomyeong.text = tranData.getGeoraecheomyeongHP()
        binding.nappumcheomyeong.text = tranData.getNappumcheomyeongHP()
        binding.bigo.text = tranData.getBigoHP()
        binding.txtCount.text = "(${tranData.returnGeoraedetail().size}건)"



        tranData.returnGeoraedetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.jungyojajeyeobu.isVisible = true
                binding.serialDetail.isVisible = true
            }
            else {
                binding.jungyojajeyeobu.isVisible = false
                binding.serialDetail.isVisible = false
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
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (masterData.getCompanyCode()[position].code == "0001") {
                        spinnerWareHouseAdapter.setList(masterData.getGwangmyeongCode())
                    }

                    if (masterData.getCompanyCode()[position].code == "0002") {
                        spinnerWareHouseAdapter.setList(masterData.getGumiCode())
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

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
        dialogEdit.show(supportFragmentManager, "EditDialog")

    }


    override fun onBackPressed() {

        backDialog()

    }


}