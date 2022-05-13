package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener

class KittingDetailListViewHolder(parent: ViewGroup, val listener : KittingDetailEditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.kitting_detail_list_item, parent, false)
) {

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    fun bind() {

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }


    }

}