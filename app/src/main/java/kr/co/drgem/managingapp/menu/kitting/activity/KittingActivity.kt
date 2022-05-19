package kr.co.drgem.managingapp.menu.kitting.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityKittingBinding
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingListAdapter
import java.text.SimpleDateFormat
import java.util.*

class KittingActivity : BaseActivity() {

    lateinit var binding : ActivityKittingBinding
    lateinit var mAdapter : KittingListAdapter

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

        var calStart = ""
        var calEnd = ""

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

        binding.btnCompanyRemove.setOnClickListener {
            binding.edtCompany.text = null
        }

        binding.btnWarehouseRemove.setOnClickListener {
            binding.edtWarehouse.text = null
        }

        binding.txtTitle.text = HtmlCompat.fromHtml(getString(R.string.kittingName, "홍길동"), HtmlCompat.FROM_HTML_MODE_LEGACY)


    }

    override fun setValues() {

        mAdapter = KittingListAdapter()
        binding.recyclerView.adapter = mAdapter
    }
}