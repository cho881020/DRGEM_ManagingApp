package kr.co.drgem.managingapp.menu.order.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.BaljuData
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.MasterDataResponse

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

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, OrderDetailDetailActivity::class.java)
            myIntent.putExtra("masterData", masterData)
            myIntent.putExtra("baljubeonho", baljuData.getBaljubeonhoHP())
            itemView.context.startActivity(myIntent)
        }

        georaecheomyeong.text = baljuData.getGeoraecheomyeongHP()
        baljubeonho.text = baljuData.getBaljubeonhoHP()
        georaecheocode.text = baljuData.getGeoraecheocodeHP()
        baljuil.text = baljuData.getBaljuilHP()
        nappumjangso.text = baljuData.getNappumjangsoHP()
        bigo.text = baljuData.getbigoHP()




    }

}