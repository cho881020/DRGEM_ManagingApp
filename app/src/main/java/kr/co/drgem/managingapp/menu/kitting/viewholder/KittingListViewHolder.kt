package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.menu.kitting.activity.KittingDetailActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.Kittingdetail
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KittingListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.kitting_list_item, parent, false)
) {

    val kittingbeonho = itemView.findViewById<TextView>(R.id.kittingbeonho)
    val kittingdeungrokil = itemView.findViewById<TextView>(R.id.kittingdeungrokil)
    val kittingja = itemView.findViewById<TextView>(R.id.kittingja)
    val bulchulchanggo = itemView.findViewById<TextView>(R.id.bulchulchanggo)
    val bigo = itemView.findViewById<TextView>(R.id.bigo)
    val yocheongbeonho = itemView.findViewById<TextView>(R.id.yocheongbeonho)
    val chulgojisicode = itemView.findViewById<TextView>(R.id.chulgojisicode)
    val deviceinfo = itemView.findViewById<TextView>(R.id.deviceinfo)


    fun bind(data : Kittingdetail){

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
                "pid" to "03",
                "tablet_ip" to IPUtil.getIpAddress(),
                "sawoncode" to sawonCode,
                "status" to "111",
            )

            Log.d("yj", "orderViewholder tabletIp : ${IPUtil.getIpAddress()}")


            apiList.postRequestSEQ(SEQMap).enqueue(object : Callback<WorkResponse> {

                override fun onResponse(call: Call<WorkResponse>, response: Response<WorkResponse>) {

                    if(response.isSuccessful){
                        response.body()?.let {

                            if(it.resultcd == "000"){
                                val myIntent = Intent(itemView.context, KittingDetailActivity::class.java)
                                myIntent.putExtra("seq", it.seq)
                                myIntent.putExtra("kittingbeonho",data.getKittingbeonhoHP())
                                itemView.context.startActivity(myIntent)


                                Log.d("yj", "SEQ성공 : ${it.resultmsg}")

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


        kittingbeonho.text = data.getKittingbeonhoHP()
        kittingdeungrokil.text = data.getKittingdeungrokilHP()
        kittingja.text = data.getKittingjaHP()
        bulchulchanggo.text = data.getBulchulchanggoHP()
        bigo.text = data.getBigoHP()
        yocheongbeonho.text = data.getYocheongbeonhoHP()
        chulgojisicode.text = data.getChulgojisicodeHP()
        deviceinfo.text = data.getDeviceinfoHP()



    }

}