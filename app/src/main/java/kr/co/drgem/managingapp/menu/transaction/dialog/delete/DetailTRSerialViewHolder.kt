package kr.co.drgem.managingapp.menu.transaction.dialog.delete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R

class DetailTRSerialViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.viewholder_detail_tr_serial, parent, false)
) {

    val addSerialRecyclerView = itemView.findViewById<RecyclerView>(R.id.addSerialRecyclerView)


    init {
        val mAdapter = DialogDetailSerialListAdapter()
        addSerialRecyclerView.adapter = mAdapter
    }


    fun bind(){




    }

}