package kr.co.drgem.managingapp.menu.order.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.models.BaljuData

class OrderDetailListViewHolder(
    parent: ViewGroup,
    val listener : OrderDetailEditListener,) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_detail_list_item, parent, false)
) {

    val txtSeq = itemView.findViewById<TextView>(R.id.txtSeq)
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)

    fun bind(data: BaljuData) {

        txtSeq.text = data.seq
        edtCount.setText(data.giipgosuryang)

        itemView.setOnClickListener {
            edtCount.requestFocus()
        }

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }

        Log.d("중요자재여부", data.jungyojajeyeobu)

        if (data.jungyojajeyeobu == "Y") {
            btnEdit.visibility = View.VISIBLE
        }
        else {
            btnEdit.visibility = View.INVISIBLE
        }


    }

}