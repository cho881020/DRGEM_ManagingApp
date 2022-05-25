package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMenuBinding
import kr.co.drgem.managingapp.menu.kitting.activity.KittingActivity
import kr.co.drgem.managingapp.menu.location.activity.LocationActivity
import kr.co.drgem.managingapp.menu.notdelivery.activity.NotDeliveryActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderActivity
import kr.co.drgem.managingapp.menu.request.activity.RequestActivity
import kr.co.drgem.managingapp.menu.stock.activity.StockActivity
import kr.co.drgem.managingapp.menu.transaction.activity.TransactionActivity
import kr.co.drgem.managingapp.models.MasterDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : BaseActivity() {

    lateinit var binding : ActivityMenuBinding
    lateinit var masterData : MasterDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu)

//        getRequestMasterCode()

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.transaction.setOnClickListener{
            val myIntent = Intent(this, TransactionActivity::class.java)
            startActivity(myIntent)
        }

        binding.orderReceipt.setOnClickListener{
            val myIntent = Intent(this, OrderActivity::class.java)
            startActivity(myIntent)
        }

        binding.kitting.setOnClickListener{
            val myIntent = Intent(this, KittingActivity::class.java)
            startActivity(myIntent)
        }

        binding.request.setOnClickListener{
            val myIntent = Intent(this, RequestActivity::class.java)
            startActivity(myIntent)
        }

        binding.notDelivery.setOnClickListener{
            val myIntent = Intent(this, NotDeliveryActivity::class.java)
            startActivity(myIntent)
        }

        binding.location.setOnClickListener{
            val myIntent = Intent(this, LocationActivity::class.java)
            startActivity(myIntent)
        }

        binding.stock.setOnClickListener{
            val myIntent = Intent(this, StockActivity::class.java)
            startActivity(myIntent)
        }


    }

    override fun setValues() {

        val userName = intent.getStringExtra("name")
        binding.sawonmyeong.text = "$userName 님"

    }

//    fun getRequestMasterCode(){
//
//        val data = hashMapOf(
//            "requesttype" to "09001",
//            "mastertype" to "all",
//        )
//
//        apiList.getRequestMasterData(data).enqueue(object : Callback<MasterDataResponse>{
//            override fun onResponse(
//                call: Call<MasterDataResponse>,
//                response: Response<MasterDataResponse>
//            ) {
//                Log.d("yj", "body : ${response.body()}")
//                if(response.isSuccessful){
//                    response.body()?.let {
//                        masterData = it
//                        Log.d("yj", "it : $it")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<MasterDataResponse>, t: Throwable) {
//                Log.d("yj", "body : ${t.message}")
//            }
//
//        })
//    }

}