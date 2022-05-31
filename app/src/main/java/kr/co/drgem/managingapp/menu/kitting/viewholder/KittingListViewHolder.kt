package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.activity.KittingDetailActivity
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.Kittingdetail

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

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, KittingDetailActivity::class.java)
            myIntent.putExtra("kittingbeonho",data.getKittingbeonhoHP())
            itemView.context.startActivity(myIntent)
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