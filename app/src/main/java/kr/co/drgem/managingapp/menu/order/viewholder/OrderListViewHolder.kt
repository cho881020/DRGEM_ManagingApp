package kr.co.drgem.managingapp.menu.order.viewholder

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.localdb.DBHelper
import kr.co.drgem.managingapp.localdb.SQLiteDB
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListViewHolder(val parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
) {


    val georaecheomyeong = itemView.findViewById<TextView>(R.id.georaecheomyeong)
    val baljubeonho = itemView.findViewById<TextView>(R.id.baljubeonho)
    val georaecheocode = itemView.findViewById<TextView>(R.id.georaecheocode)
    val baljuil = itemView.findViewById<TextView>(R.id.baljuil)
    val nappumjangso = itemView.findViewById<TextView>(R.id.nappumjangso)
    val bigo = itemView.findViewById<TextView>(R.id.bigo)
    val seq = itemView.findViewById<TextView>(R.id.seq)

    fun bind(baljuData: Baljubeonho, position: Int){


        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        itemView.setOnClickListener{

            var sawonCode = ""
                LoginUserUtil.getLoginData()?.let {
                    sawonCode = it.sawoncode.toString()
                }

            val SEQMap = hashMapOf(
                "requesttype" to "08001",
                "pid" to "02",
                "tablet_ip" to IPUtil.getIpAddress(),
                "sawoncode" to sawonCode,
                "status" to "111",
            )

            Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")


            apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse>{

                override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                    if(response.isSuccessful){
                        response.body()?.let {

                            if(it.resultcd == "000"){
                                val myIntent = Intent(itemView.context, OrderDetailDetailActivity::class.java)
                                myIntent.putExtra("baljubeonho", baljuData.getBaljubeonhoHP())
                                myIntent.putExtra("seq", it.seq)
                                itemView.context.startActivity(myIntent)

                                val dbHelper = DBHelper(parent.context, "drgemdb.db", null, 1)
                                val mSqliteDB = SQLiteDB()
                                mSqliteDB.makeDb(dbHelper.writableDatabase)

                                mSqliteDB.updateWorkInfo("02", it.seq, it.status)

                                Log.d("yj", "SEQ : ${it.seq}")
                                Log.d("yj", "it : $it")
                            }

                            else{
                                Log.d("yj", "SEQ ?????? ?????? : ${it.resultmsg}")
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "SEQ ?????? ?????? : ${t.message}")
                }

            })

        }


        georaecheomyeong.text = baljuData.getGeoraecheomyeongHP()
        baljubeonho.text = baljuData.getBaljubeonhoHP()
        georaecheocode.text = baljuData.getGeoraecheocodeHP()
        baljuil.text = baljuData.getBaljuilHP()
        nappumjangso.text = baljuData.getNappumjangsoHP()
        bigo.text = baljuData.getbigoHP()
        seq.text = "${position+1}"

    }

}