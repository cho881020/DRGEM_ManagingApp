package kr.co.drgem.managingapp.menu.order.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.databinding.ActivityOrderDetailBinding
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.menu.order.dialog.OrderDetailDialog
import kr.co.drgem.managingapp.models.BaljuData
import kr.co.drgem.managingapp.roomdb.datas.BaljuRoomData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailDetailActivity : BaseActivity(), OrderDetailEditListener {

    lateinit var binding : ActivityOrderDetailBinding
    lateinit var mAdapter : OrderDetailListAdapter
    val dialog = OrderDetailDialog()

    val mBaljuList = ArrayList<BaljuData>()

    lateinit var mBaljubeonho : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_detail)

        mBaljubeonho = intent.getStringExtra("baljubeonho")!!
        setupEvents()
        setValues()

    }

    override fun setupEvents() {


        binding.btnNameRemove.setOnClickListener {
            binding.edtName.text = null
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val cal = Calendar.getInstance()
        val dateServer = SimpleDateFormat("yyyyMMdd")  // 서버 전달 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 텍스트뷰 포맷
        binding.txtDate.text = dateFormat.format(cal.time)


        var calDate = ""
        binding.layoutDate.setOnClickListener {

            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

                    calDate = dateServer.format(cal.time)
                    binding.txtDate.text = dateFormat.format(cal.time)

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


    }

    override fun setValues() {



        mAdapter = OrderDetailListAdapter(this, mContext, mBaljuList)
        binding.recyclerView.adapter = mAdapter

//        임시로
        getAllBaljuListFromRoomDB()

        if (mBaljuList.size == 0) {

            mBaljuList.add(  BaljuData("1", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "10", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("2", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "12", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("3", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "13", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("4", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "4", "Y", "창고5")  )
            mBaljuList.add(  BaljuData("5", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "4", "N", "창고5")  )
            mBaljuList.add(  BaljuData("6", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "4", "N", "창고5")  )
            mBaljuList.add(  BaljuData("7", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "4", "N", "창고5")  )
            mBaljuList.add(  BaljuData("8", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "N", "창고5")  )
            mBaljuList.add(  BaljuData("9", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "N", "창고1")  )
            mBaljuList.add(  BaljuData("10", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("11", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "6", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("12", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "6", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("13", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "6", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("14", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "6", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("15", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "6", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("16", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고1")  )
            mBaljuList.add(  BaljuData("17", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고1")  )
            mBaljuList.add(  BaljuData("18", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고1")  )
            mBaljuList.add(  BaljuData("19", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고1")  )
            mBaljuList.add(  BaljuData("20", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고1")  )
            mBaljuList.add(  BaljuData("21", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고2")  )
            mBaljuList.add(  BaljuData("22", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "8", "N", "창고2")  )
            mBaljuList.add(  BaljuData("23", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("24", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고2")  )
            mBaljuList.add(  BaljuData("25", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("26", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고1")  )
            mBaljuList.add(  BaljuData("27", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고5")  )
            mBaljuList.add(  BaljuData("28", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고5")  )
            mBaljuList.add(  BaljuData("29", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고5")  )
            mBaljuList.add(  BaljuData("30", "품목코드1", "품명1", "도번모델1", "사양1", "발주단위1", "발주수량1", "입고예정일1", "5", "Y", "창고5")  )


            clearDbAndInsertAllSearchedData()
        }



    }

    private fun clearDbAndInsertAllSearchedData() {

        roomDB.baljuDao().deleteBaljuListByBaljubunho(mBaljubeonho)


        val roomBaljuDataList = ArrayList<BaljuRoomData>()
        for (item in mBaljuList) {

            roomBaljuDataList.add(item.toBaljuRoomData(mBaljubeonho))

        }
        roomDB.baljuDao().insertBaljuList(roomBaljuDataList)


        getAllBaljuListFromRoomDB()

    }

    fun getAllBaljuListFromRoomDB() {

        val dbList = roomDB.baljuDao().getBaljuListByBaljubunho(mBaljubeonho)

        mBaljuList.clear()
        for (item in dbList) {
            mBaljuList.add(item.toBaljuData())

            Log.d("item", item.toString())
        }

        Log.d("DB를 통한 목록 개수", mBaljuList.size.toString())
        mAdapter.notifyDataSetChanged()

    }

    override fun onClickedEdit() {
        dialog.show(supportFragmentManager, "EditDialog")
    }
}