package kr.co.drgem.managingapp.menu.order.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener

class OrderDetailListViewHolder(parent: ViewGroup, val listener : OrderDetailEditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_detail_list_item, parent, false)
) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)

    fun bind() {

        itemView.setOnClickListener {
            edtCount.requestFocus()
        }

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }



    }

}