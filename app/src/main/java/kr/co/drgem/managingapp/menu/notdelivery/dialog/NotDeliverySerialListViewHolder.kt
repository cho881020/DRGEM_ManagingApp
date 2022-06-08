package kr.co.drgem.managingapp.menu.notdelivery.dialog

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity

class NotDeliverySerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_not_delivery_serial_list, parent, false)
) {
    val txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)

    fun bind(position: Int) {

        txtNumber.text = "${position+1}"

    }

}