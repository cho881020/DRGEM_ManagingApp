package kr.co.drgem.managingapp.menu.notdelivery.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener

class NotDeliveryListViewHolder(parent: ViewGroup, val listener: NotDeliveryEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.notdelivery_list_item, parent, false)
    ) {

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    fun bind() {

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }

    }

}