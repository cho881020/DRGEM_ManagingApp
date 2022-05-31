package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingListAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting)


        setupEvents()
        setValues()
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

        var calStart = dateSet.format(cal.time)
        var calEnd = dateSet.format(cal.time)

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
            ).show()

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
            ).show()

        }

        binding.btnFind.setOnClickListener {
            binding.layoutList.isVisible = true
            binding.layoutEmpty.isVisible = false
        }

        binding.btnKittingjaRemove.setOnClickListener {
            binding.edtKittingja.text = null
        }

        binding.btnCanggocodeRemove.setOnClickListener {
            binding.edtChanggocode.text = null
        }

        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.kittingName, "홍길동"), HtmlCompat.FROM_HTML_MODE_LEGACY)


        binding.btnFind.setOnClickListener {

            val inputKittingja = binding.edtKittingja.text.toString()
            val inputChanggocode = binding.edtChanggocode.text.toString()

            apiList.getRequestKittingNumber("02501", calStart, calEnd, inputKittingja, inputChanggocode).enqueue(object :
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
                                mAdapter.setList(it.returnKittingDetail())

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

        mAdapter = KittingListAdapter()
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${kittingData.returnKittingDetail().size}건)"

    }




}