package kr.co.drgem.managingapp.menu.location.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R

class LocationListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.location_list_item, parent, false)
) {

    val edtWarehouse = itemView.findViewById<EditText>(R.id.edtWarehouse)

    fun bind() {

        itemView.setOnClickListener {
            edtWarehouse.requestFocus()
        }

    }

}