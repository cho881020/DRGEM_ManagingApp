package kr.co.drgem.managingapp.menu.request.viewholder

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
import kr.co.drgem.managingapp.menu.request.activity.RequestDetailActivity
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.models.Yocheongdetail
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.request_list_item, parent, false)
) {

    val yocheongil = itemView.findViewById<TextView>(R.id.yocheongil)
    val yocheongbeonho = itemView.findViewById<TextView>(R.id.yocheongbeonho)
    val yocheongchanggo = itemView.findViewById<TextView>(R.id.yocheongchanggo)
    val yocheongja = itemView.findViewById<TextView>(R.id.yocheongja)
    val bigo = itemView.findViewById<TextView>(R.id.bigo)
    val chulgojisicode = itemView.findViewById<TextView>(R.id.chulgojisicode)
    val gongheong = itemView.findViewById<TextView>(R.id.gongheong)

    fun bind(data: Yocheongdetail, companyCode: String, wareHouseCode: String){

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
                "pid" to "04",
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

                                val myIntent = Intent(itemView.context, RequestDetailActivity::class.java)
                                myIntent.putExtra("yocheongbeonho", data.getyocheongbeonhoHP())
                                myIntent.putExtra("companyCode", companyCode)
                                myIntent.putExtra("wareHouseCode", wareHouseCode)
                                myIntent.putExtra("seq", it.seq)
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


        yocheongil.text = data.getyocheongilHP()
        yocheongbeonho.text = data.getyocheongbeonhoHP()
        yocheongchanggo.text = data.getyocheongchanggoHP()
        yocheongja.text = data.getyocheongjaHP()
        bigo.text = data.getbigoHP()
        chulgojisicode.text = data.getchulgojisicodeHP()
        gongheong.text = data.getgongheongHP()

    }

}