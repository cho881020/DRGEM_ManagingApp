package kr.co.drgem.managingapp.menu.kitting.activity

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityKittingDetailBinding
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.adapter.KittingDetailListAdapter
import kr.co.drgem.managingapp.menu.kitting.dialog.KittingDetailDialog
import kr.co.drgem.managingapp.models.KittingDetailResponse
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

        binding.radio2.setOnClickListener {
            johoejogeon = "2"
            getRequestKittingDetail()
        }

    }

    fun getRequestKittingDetail() {

        apiList.getRequestKittingDetail("02502", mkittingbeonho, johoejogeon ,migwanri ).enqueue(object : Callback<KittingDetailResponse>{
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

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "Kitting_dialog")
    }
}