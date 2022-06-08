package kr.co.drgem.managingapp.menu.stock.activity

import android.os.Bundle
import android.util.Log
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

    lateinit var productData: ProductInfoResponse
    var inputCode = ""

    val mList: ArrayList<Pummokdetail> = arrayListOf()
    var addList: ArrayList<Pummokdetail> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock)

        setupEvents()

    }

    override fun onBackPressed() {
        backDialog()
    }

    override fun setupEvents() {

        binding.btnBack.setOnClickListener {
            backDialog()
        }

        binding.btnSave.setOnClickListener {
            saveDialog()
        }

        binding.btnCodeRemove.setOnClickListener {
            binding.edtCode.text = null
        }


        binding.btnFind.setOnClickListener {

            inputCode = binding.edtCode.text.toString()
            if (inputCode.isEmpty()) {
                Toast.makeText(mContext, "요청코드를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apiList.getRequestProductinfo("02091", inputCode)
                .enqueue(object : Callback<ProductInfoResponse> {
                    override fun onResponse(
                        call: Call<ProductInfoResponse>,
                        response: Response<ProductInfoResponse>
                    ) {
                        if (response.isSuccessful) {

                            response.body()?.let {


                                if (it.returnPummokDetail().size == 0) {
                                    Toast.makeText(mContext, "검색된 내역이 없습니다.", Toast.LENGTH_SHORT)
                                        .show()

                                } else {
                                    productData = it
                                    addList.clear()
                                    addList.addAll(it.returnPummokDetail())
                                    Log.d("yj", "addList : $addList")

                                    binding.layoutEmpty.isVisible = false
                                    binding.layoutFind.isVisible = false
                                    binding.layoutAdd.isVisible = true
                                    binding.layoutList.isVisible = true

                                    setText()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ProductInfoResponse>, t: Throwable) {

                    }
                })

        }

        binding.btnAdd.setOnClickListener {

            addList[0].hyeonjaegosuryang = binding.hyeonjaegosuryang.text.toString()

            mList.forEach {
                if (it.pummokcode == addList[0].pummokcode) {
                    Toast.makeText(mContext, "이미 작성 된 품목입니다.", Toast.LENGTH_SHORT).show()

                    addList.clear()
                    binding.layoutAdd.isVisible = false
                    binding.layoutFind.isVisible = true

                }
            }

            mList.addAll(addList)
            addList.clear()

            Log.d("yj", "mList : $mList")
            setValues()

            binding.layoutAdd.isVisible = false
            binding.layoutFind.isVisible = true

        }

    }

    override fun setValues() {
        mAdapter = StockListAdapter(mList)
        binding.recyclerView.adapter = mAdapter

    }

    fun setText() {
        binding.pummokcode.text = (inputCode)
        binding.pummyeong.text = productData.returnPummokDetail()[0].getPummokcodeHP()
        binding.dobeonModel.text = productData.returnPummokDetail()[0].getdobeon_modelHP()
        binding.saying.text = productData.returnPummokDetail()[0].getsayingHP()
        binding.hyeonjaegosuryang.setText(productData.returnPummokDetail()[0].gethyeonjaegosuryangHP())
    }


}
