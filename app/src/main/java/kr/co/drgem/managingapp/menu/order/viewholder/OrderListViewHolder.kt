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
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.*
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
) {


    val georaecheomyeong = itemView.findViewById<TextView>(R.id.georaecheomyeong)
    val baljubeonho = itemView.findViewById<TextView>(R.id.baljubeonho)
    val georaecheocode = itemView.findViewById<TextView>(R.id.georaecheocode)
    val baljuil = itemView.findViewById<TextView>(R.id.baljuil)
    val nappumjangso = itemView.findViewById<TextView>(R.id.nappumjangso)
    val bigo = itemView.findViewById<TextView>(R.id.bigo)

    fun bind(baljuData : Baljubeonho, masterData : MasterDataResponse){


        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        itemView.setOnClickListener{

            var sawonCode = ""
                LoginUserUtil.getLoginData()?.let {
                    sawonCode = it.sawoncode.toString()
                }

            // TODO - API 정상 연동시 수정
            val SEQMap = hashMapOf(
                "requesttype" to "",
                "pid" to "02",
                "tablet_ip" to "000",
                "sawoncode" to sawonCode,
                "status" to "111",
            )

            Log.d("yj", "SEQMap : $SEQMap")

            apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse>{

                override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                    if(response.isSuccessful){
                        response.body()?.let {

                            if(it.resultcd == "000"){
                                val myIntent = Intent(itemView.context, OrderDetailDetailActivity::class.java)
                                myIntent.putExtra("masterData", masterData)
                                myIntent.putExtra("baljubeonho", baljuData.getBaljubeonhoHP())
                                itemView.context.startActivity(myIntent)
                            }

                            else{
                                Log.d("yj", "SEQ 실패 코드 : ${it.resultmsg}")
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                    Log.d("yj", "SEQ 서버 실패 : ${t.message}")
                }

            })

        }

        georaecheomyeong.text = baljuData.getGeoraecheomyeongHP()
        baljubeonho.text = baljuData.getBaljubeonhoHP()
        georaecheocode.text = baljuData.getGeoraecheocodeHP()
        baljuil.text = baljuData.getBaljuilHP()
        nappumjangso.text = baljuData.getNappumjangsoHP()
        bigo.text = baljuData.getbigoHP()

    }

}