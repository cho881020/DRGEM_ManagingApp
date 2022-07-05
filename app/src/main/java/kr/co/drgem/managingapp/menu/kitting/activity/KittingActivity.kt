/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingActivity
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 키팅출고 화면으로 키팅번호요청 기능
 */

package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.LoadingDialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingListAdapter
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.KittingResponse
import kr.co.drgem.managingapp.models.SawonData
import kr.co.drgem.managingapp.utils.SawonDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class KittingActivity : BaseActivity() {

    lateinit var binding: ActivityKittingBinding
    lateinit var mAdapter: KittingListAdapter
    lateinit var kittingData: KittingResponse
    val loadingDialog = LoadingDialogFragment()

    var changgocode = ""
    var calStart = ""
    var calEnd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting)


        setupEvents()
        getRequestKitting()
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

        binding.txtDateStart.text = dateFormat.format(cal.time)
        binding.txtDateEnd.text = dateFormat.format(cal.time)

        calStart = dateSet.format(cal.time)
        calEnd = dateSet.format(cal.time)

        binding.layoutDateStart.setOnClickListener {

            val date = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year, month, dayOfMonth)

                    calStart = dateSet.format(cal.time)
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


        val changgoList = ArrayList<Detailcode>()
        changgoList.add(Detailcode("", "전체"))
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


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }



    }


    //    키팅번호요청
    fun getRequestKitting() {
        binding.btnFind.setOnClickListener {

            val kittingja = binding.autoCompleteTextView.text.toString()
            loadingDialog.show(supportFragmentManager, null)

            apiList.getRequestKittingNumber("02501", calStart, calEnd, kittingja, changgocode)
                .enqueue(object :
                    Callback<KittingResponse> {
                    override fun onResponse(
                        call: Call<KittingResponse>,
                        response: Response<KittingResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                kittingData = it
                                setValues()

                                if (it.returnKittingDetail().size == 0) {
                                    searchZeroDialog()
                                    mAdapter.clearList()
                                    binding.txtCount.text = "(0건)"

                                } else {

                                    binding.layoutList.isVisible = true
                                    binding.layoutEmpty.isVisible = false

                                }
                            }
                        }
                        loadingDialog.dismiss()
                    }

                    override fun onFailure(call: Call<KittingResponse>, t: Throwable) {
                        loadingDialog.dismiss()
                        serverErrorDialog("${t.message}\n 관리자에게 문의하세요.")
                    }

                })

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

    override fun setValues() {

        mAdapter = KittingListAdapter()
        mAdapter.setList(kittingData.returnKittingDetail())
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${kittingData.returnKittingDetail().size}건)"

    }


}