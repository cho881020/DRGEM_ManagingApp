package kr.co.drgem.managingapp.menu.request.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.request.activity.RequestDetailActivity
import kr.co.drgem.managingapp.models.Yocheongdetail

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

    fun bind(data : Yocheongdetail){

        itemView.setOnClickListener{
            val myIntent = Intent(itemView.context, RequestDetailActivity::class.java)
            myIntent.putExtra("yocheongbeonho", data.getyocheongbeonhoHP())
            itemView.context.startActivity(myIntent)
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