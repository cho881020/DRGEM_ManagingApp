package kr.co.drgem.managingapp

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.drgem.managingapp.databinding.ActivityMenuBinding
import kr.co.drgem.managingapp.menu.kitting.activity.KittingActivity
import kr.co.drgem.managingapp.menu.location.LocationActivity
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderReceiptActivity
import kr.co.drgem.managingapp.menu.request.activity.RequestActivity
import kr.co.drgem.managingapp.menu.stock.StockActivity
import kr.co.drgem.managingapp.menu.transaction.activity.TransactionActivity

class MenuActivity : BaseActivity() {

    lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.tradingStatement.setOnClickListener{
            val myIntent = Intent(this, TransactionActivity::class.java)
            startActivity(myIntent)
        }

        binding.orderReceipt.setOnClickListener{
            val myIntent = Intent(this, OrderReceiptActivity::class.java)
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

        binding.notShipped.setOnClickListener{
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

    }
}