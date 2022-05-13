package kr.co.drgem.managingapp.menu.request.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener

class RequestDetailListViewHolder(parent: ViewGroup, val listener : RequestDetailEditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.request_detail_list_item, parent, false)
) {

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    fun bind() {

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }


    }

}