package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingListAdapter
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.KittingResponse
import kr.co.drgem.managingapp.models.TranResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class KittingActivity : BaseActivity() {

    lateinit var binding : ActivityKittingBinding
    lateinit var mAdapter : KittingListAdapter
    lateinit  var kittingData : KittingResponse

    var changgocode = ""
    var calStart = ""
    var calEnd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting)


        setupEvents()
    }

    override fun setupEvents() {

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

            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

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
            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

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

        binding.btnFind.setOnClickListener {
            binding.layoutList.isVisible = true
            binding.layoutEmpty.isVisible = false
        }

        binding.btnKittingjaRemove.setOnClickListener {
            binding.edtKittingja.text = null
        }

        val changgoList = ArrayList<Detailcode>()
        changgoList.add(Detailcode("2001", "자재창고1"))
        changgoList.add(Detailcode("2014", "자재창고2"))

        val spinnerAdapter = MasterDataSpinnerAdapter(mContext, R.layout.spinner_list_item, changgoList)
        binding.spinnerChanggocode.adapter = spinnerAdapter

        binding.spinnerChanggocode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {

                changgocode = changgoList[position].code
                Log.d("yj", "창고코드 : $changgocode")

                getRequestKitting()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    fun getRequestKitting(){
        binding.btnFind.setOnClickListener {

            val inputKittingja = binding.edtKittingja.text.toString()

            apiList.getRequestKittingNumber("02501", calStart, calEnd, inputKittingja, changgocode).enqueue(object :
                Callback<KittingResponse>{
                override fun onResponse(
                    call: Call<KittingResponse>,
                    response: Response<KittingResponse>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {

                            if(it.returnKittingDetail().size == 0){
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()

                            }
                            else {
                                kittingData = it

                                setValues()

                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false

                            }


                        }
                    }
                }

                override fun onFailure(call: Call<KittingResponse>, t: Throwable) {
                    Log.d("yj", "키팅번호요청실패 : ${t.message}" )
                }

            })

        }
    }

    override fun setValues() {

        mAdapter = KittingListAdapter(kittingData.returnKittingDetail())
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${kittingData.returnKittingDetail().size}건)"

    }




}