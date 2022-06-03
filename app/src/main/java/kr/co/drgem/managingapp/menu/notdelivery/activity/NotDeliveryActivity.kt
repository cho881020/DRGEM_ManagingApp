package kr.co.drgem.managingapp.menu.notdelivery.activity

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
import kr.co.drgem.managingapp.databinding.ActivityNotDeliveryBinding
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.adapter.NotDeliveryListAdapter
import kr.co.drgem.managingapp.menu.notdelivery.dialog.NotDeliveryDialog
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.MainDataManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NotDeliveryActivity : BaseActivity(), NotDeliveryEditListener {

    lateinit var binding: ActivityNotDeliveryBinding
    lateinit var mAdapter: NotDeliveryListAdapter
    lateinit var notDeliveryData: NotDeliveryResponse
    val dialog = NotDeliveryDialog()

    var calStart = ""
    var calEnd = ""
    var companyCode = "0001"
    var wareHouseCode = "1001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()
    var migwanri = "0"

    lateinit var mYocheongbeonho: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivery)

        setupEvents()
        spinnerSet()
        getRequestNotDelivery()


    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        val cal = Calendar.getInstance()
        val dateSet = SimpleDateFormat("yyyyMMdd")
        val dateFormat = SimpleDateFormat("MM-dd")

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

        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }


        binding.btnOutName.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInName.setOnClickListener {
            binding.edtInName.text = null
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                migwanri = "0"
            } else {
                migwanri = "1"
            }
            getRequestNotDelivery()
        }




    }

    fun getRequestNotDelivery(){

        binding.btnFind.setOnClickListener {

            val yocheongja = binding.edtName.text.toString()
            if(yocheongja.isEmpty()){
                Toast.makeText(mContext, "요청자를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var yocheongpummok = binding.edtCode.text.toString()
            if(yocheongpummok.isEmpty()){
                yocheongpummok = "1"
            }


            apiList.getRequestNotDeliveryDetail("02071", calStart, calEnd, companyCode, wareHouseCode, yocheongja, yocheongpummok, migwanri).enqueue(object :Callback<NotDeliveryResponse>{
                override fun onResponse(
                    call: Call<NotDeliveryResponse>,
                    response: Response<NotDeliveryResponse>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            if(it.returnPummokdetailDetail().size == 0){
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                notDeliveryData = it

                                setValues()

                                binding.layoutList.isVisible = true
                                binding.layoutEmpty.isVisible = false
                            }

                        }



                    }

                }

                override fun onFailure(call: Call<NotDeliveryResponse>, t: Throwable) {
                    Log.d("yj", "미출고명세 실패 : ${t.message}")
                }

            })

        }
    }

    override fun setValues() {

        mAdapter = NotDeliveryListAdapter(notDeliveryData.returnPummokdetailDetail(),this)
        binding.recyclerView.adapter = mAdapter

        binding.txtCount.text = "(${notDeliveryData.pummokcount} 건)"

        notDeliveryData.returnPummokdetailDetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.jungyojajeyeobu.isVisible = true
                binding.serialDetail.isVisible = true
            }
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
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

        }

    }


    override fun onClickedEdit(count : Int, data: PummokdetailDelivery) {
        dialog.setCount(count, data)
        dialog.show(supportFragmentManager, "dialog_notDelivery")
    }
}