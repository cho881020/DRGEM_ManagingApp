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
import kr.co.drgem.managingapp.models.Baljudetail

class OrderDetailListViewHolder(
    parent: ViewGroup,
    val listener : OrderDetailEditListener,) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_detail_list_item, parent, false)
) {

    val txtSeq = itemView.findViewById<TextView>(R.id.txtSeq)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val baljusuryang = itemView.findViewById<TextView>(R.id.baljusuryang)
    val ipgoyejeongil = itemView.findViewById<TextView>(R.id.ipgoyejeongil)

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)


    fun bind(data: Baljudetail) {

        txtSeq.text = data.seq
        pummokcode.text = data.pummokcode
        pummyeong.text = data.pummyeong
        dobeon_model.text = data.dobeon_model
        baljusuryang.text = data.baljusuryang
        ipgoyejeongil.text = data.ipgoyejeongil


        itemView.setOnClickListener {
            edtCount.requestFocus()
        }

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }


//        if (data.jungyojajeyeobu == "Y") {
//            btnEdit.visibility = View.VISIBLE
//        }
//        else {
//            btnEdit.visibility = View.INVISIBLE
//        }


    }

}