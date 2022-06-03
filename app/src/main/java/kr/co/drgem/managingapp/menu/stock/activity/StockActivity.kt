package kr.co.drgem.managingapp.menu.stock.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityStockBinding
import kr.co.drgem.managingapp.menu.stock.adapter.StockListAdapter
import kr.co.drgem.managingapp.models.ProductInfoResponse
import kr.co.drgem.managingapp.models.Pummokdetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockActivity : BaseActivity() {

    lateinit var binding: ActivityStockBinding
    lateinit var mAdapter: StockListAdapter
    val mList = ArrayList<Pummokdetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }

        binding.btnAdd.setOnClickListener {
            binding.layoutEmpty.isVisible = true
            binding.layoutAdd.isVisible = false
            binding.layoutList.isVisible = false
        }

        binding.btnFind.setOnClickListener {

            val inputCode = binding.edtCode.text.toString()
            if(inputCode.isEmpty()){
                Toast.makeText(mContext, "요청코드를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apiList.getRequestProductinfo("02091", inputCode).enqueue(object : Callback<ProductInfoResponse>{
                override fun onResponse(
                    call: Call<ProductInfoResponse>,
                    response: Response<ProductInfoResponse>
                ) {
                    if(response.isSuccessful){

                        response.body()?.let {


                            if(it.returnPummokDetail().size == 0){
                                Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT).show()

                            }
                            else {
                                mList.clear()
                                mList.addAll(it.returnPummokDetail())
                                setValues()
                                binding.layoutEmpty.isVisible = false
                                binding.layoutAdd.isVisible = true
                                binding.layoutList.isVisible = true

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ProductInfoResponse>, t: Throwable) {

                }
            })

        }
    }

    override fun setValues() {

        mAdapter = StockListAdapter(mList)
        binding.recyclerView.adapter = mAdapter


    }
}