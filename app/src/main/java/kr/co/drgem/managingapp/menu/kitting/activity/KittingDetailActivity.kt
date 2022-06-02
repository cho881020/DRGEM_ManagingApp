package kr.co.drgem.managingapp.menu.kitting.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.models.KittingDetailResponse
import kr.co.drgem.managingapp.models.Pummokdetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KittingDetailActivity : BaseActivity(), KittingDetailEditListener {

    lateinit var binding: ActivityKittingDetailBinding
    lateinit var mAdapter: KittingDetailListAdapter
    lateinit var kittingDetailData: KittingDetailResponse

    val dialog = KittingDetailDialog()

    lateinit var mkittingbeonho: String
    var johoejogeon = "0"
    var migwanri = "0"
    var changgocode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kitting_detail)

        mkittingbeonho = intent.getStringExtra("kittingbeonho")!!

        getRequestKittingDetail()
        setupEvents()
        getRequestJohoejogeon()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnOutNameRemove.setOnClickListener {
            binding.edtOutName.text = null
        }

        binding.btnInNameRemove.setOnClickListener {
            binding.edtInName.text = null
        }

    }

    override fun setValues() {

        mAdapter = KittingDetailListAdapter(kittingDetailData.returnKittingDetail(),this)
        binding.recyclerView.adapter = mAdapter

        binding.kittingbeonho.text = "키팅번호 - $mkittingbeonho"
        binding.kittingbeonho2.text = mkittingbeonho
        binding.txtCount.text = "(${kittingDetailData.getPummokCount()} 건)"


        kittingDetailData.returnKittingDetail().forEach {
            if(it.jungyojajeyeobu == "Y"){
                binding.jungyojajeyeobu.isVisible = true
                binding.serialDetail.isVisible = true
            }
        }


    }

    fun getRequestJohoejogeon(){

        binding.radio0.setOnClickListener {
            johoejogeon = "0"
            getRequestKittingDetail()
        }

        binding.radio1.setOnClickListener {
            johoejogeon = "1"
            getRequestKittingDetail()
        }

        binding.checkMigwanri.setOnCheckedChangeListener { button, ischecked ->
            if(ischecked){
                migwanri = "0"
            }
            else{
                migwanri = "1"
            }

            getRequestKittingDetail()
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

                getRequestKittingDetail()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


    }



    fun getRequestKittingDetail() {

        apiList.getRequestKittingDetail("02502", mkittingbeonho, johoejogeon ,migwanri, changgocode).enqueue(object : Callback<KittingDetailResponse>{
            override fun onResponse(
                call: Call<KittingDetailResponse>,
                response: Response<KittingDetailResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let{
                        kittingDetailData = it

                        setValues()

                    }
                }
            }

            override fun onFailure(call: Call<KittingDetailResponse>, t: Throwable) {
                Log.d("yj", "키팅명세요청실패 : ${t.message}" )
            }

        })



    }

    override fun onClickedEdit(count : Int, data: Pummokdetail) {

        dialog.setCount(mkittingbeonho, count, data)
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }
}