package kr.co.drgem.managingapp.menu.request.activity

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
import kr.co.drgem.managingapp.databinding.ActivityRequestBinding
import kr.co.drgem.managingapp.menu.request.adapter.RequestListAdapter
import kr.co.drgem.managingapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RequestActivity : BaseActivity() {

    lateinit var binding: ActivityRequestBinding
    lateinit var mAdapter: RequestListAdapter

    var mWareHouseList : ArrayList<Detailcode> = arrayListOf()
    var yocheongdetail = ArrayList<Yocheongdetail>()

    var companyCode = "0001"
    var wareHouseCode = "1001"
    var setDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request)

        setupEvents()
        spinnerSet()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        val cal = Calendar.getInstance()
        val dateSet = SimpleDateFormat("yyyyMMdd")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val txtDateFormat = SimpleDateFormat("yyyy.MM.dd")

        binding.txtDateStart.text = dateFormat.format(cal.time)
        binding.txtDateEnd.text = dateFormat.format(cal.time)

        var calStart = dateSet.format(cal.time)
        var calEnd = dateSet.format(cal.time)

        var txtCalStart =txtDateFormat.format(cal.time)
        var txtCalEnd =txtDateFormat.format(cal.time)

        binding.layoutDateStart.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calStart = dateSet.format(cal.time)
                    txtCalStart =txtDateFormat.format(cal.time)
                    binding.txtDateStart.text = dateFormat.format(cal.time)

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

        binding.layoutDateEnd.setOnClickListener {
            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calEnd = dateSet.format(cal.time)
                    txtCalEnd =txtDateFormat.format(cal.time)
                    binding.txtDateEnd.text = dateFormat.format(cal.time)
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




        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnFind.setOnClickListener {

            var yocheongja = binding.edtName.text.toString()

            if(binding.edtName.text.toString().isEmpty()){
                yocheongja = ""
            }

            setDate = "$txtCalStart ~ $txtCalEnd"

            apiList.getRequestRequestNumber("02061", calStart, calEnd, companyCode, wareHouseCode, yocheongja).enqueue(object : Callback<RequestResponse>{
                override fun onResponse(
                    call: Call<RequestResponse>,
                    response: Response<RequestResponse>
                ) {

                    if(response.isSuccessful){

                        Log.d("yj", "response.body 성공 : ${response.body()}" )

                        setValues()

                        response.body()?.let {

                            if(it.returnYocheongDetail().size == 0){
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                yocheongdetail = it.returnYocheongDetail()

                                setValues()

                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false

                            }

                        }

                    }

                }

                override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                    Log.d("yj", "요청번호요청 실패 : ${t.message}" )
               }

            })

        }

    }

    override fun setValues() {

        mAdapter = RequestListAdapter(yocheongdetail)
        binding.recyclerView.adapter = mAdapter


        if(companyCode == "0001"){
            binding.saupjangName.text = "광명본사"
        }
        else if(companyCode == "0002") {
            binding.saupjangName.text = "구미공장"
        }

        binding.txtDate.text = setDate


    }

    fun spinnerSet() {
        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse


        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, masterData.getCompanyCode())
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



}