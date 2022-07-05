/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : RequestActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 자재출고 화면으로 요청번호요청 기능
 */

package kr.co.drgem.managingapp.menu.request.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityRequestBinding
import kr.co.drgem.managingapp.menu.request.adapter.RequestListAdapter
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.SawonDataManager
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
    val loadingDialog = LoadingDialogFragment()

    var companyCode = "0002"
    var wareHouseCode = "2001"
    var setDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request)

        setupEvents()
        spinnerSet()
        completeTextView()

    }

    override fun setupEvents() {

        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, position, l ->

            val selectedSawonString = adapterView.getItemAtPosition(position) as String

            val code = selectedSawonString.split("(")[1].replace(")","")

            binding.autoCompleteTextView.setText(code)
        }


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

        //    요청번호요청
        binding.btnFind.setOnClickListener {

            loadingDialog.show(supportFragmentManager, null)

            val yocheongja = binding.autoCompleteTextView.text.toString()


            setDate = "$txtCalStart ~ $txtCalEnd"

            apiList.getRequestRequestNumber("02061", calStart, calEnd, companyCode, wareHouseCode, yocheongja).enqueue(object : Callback<RequestResponse>{
                override fun onResponse(
                    call: Call<RequestResponse>,
                    response: Response<RequestResponse>
                ) {

                    if(response.isSuccessful){


                        response.body()?.let {

                            yocheongdetail = it.returnYocheongDetail()
                            setValues()

                            if(it.returnYocheongDetail().size == 0){
                                mAdapter.clearList()
                                searchZeroDialog()
                                binding.txtCount.text = "(0건)"

                            }
                            else {

                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false

                            }
                        }
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                    serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    loadingDialog.dismiss()
               }

            })

        }

    }

    override fun setValues() {

        mAdapter = RequestListAdapter(companyCode, wareHouseCode)
        mAdapter.setList(yocheongdetail)
        binding.recyclerView.adapter = mAdapter


        if(companyCode == "0001"){
            binding.saupjangName.text = "광명본사"
        }
        else if(companyCode == "0002") {
            binding.saupjangName.text = "구미공장"
        }

        binding.txtDate.text = setDate

        binding.txtCount.text = "(${yocheongdetail.size} 건)"

    }


    fun spinnerSet() {
        val masterData = intent.getSerializableExtra("masterData") as MasterDataResponse


        val spinnerCompanyAdapter =
            MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, masterData.getCompanyCode())
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
                    Log.d("yj", "companyCode : $companyCode")
                    Log.d("yj", "waarHouseCode : $wareHouseCode")


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

    }

    private fun completeTextView(){

        val sawonmyeongList = ArrayList<String>()
        var sawonData = ArrayList<SawonData>()

        SawonDataManager.getSawonDataList()?.let{
            sawonData = it.sawon
        }

        sawonData.forEach {
            sawonmyeongList.add("${it.sawonmyeong} (${it.sawoncode})")
        }

        val autoCompleteTextView = binding.autoCompleteTextView

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sawonmyeongList)

        autoCompleteTextView.setAdapter(adapter)

    }


}

