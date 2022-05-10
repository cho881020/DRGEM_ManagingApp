package kr.co.drgem.managingapp.menu.transaction.dialog.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.adapter.DialogEditSerialListAdapter

class EditTRSerialViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.viewholder_edit_tr_serial, parent, false)
) {

    val addSerialRecyclerView = itemView.findViewById<RecyclerView>(R.id.addSerialRecyclerView)


    init {
        val mAdapter = DialogEditSerialListAdapter()
        addSerialRecyclerView.adapter = mAdapter
    }


    fun bind(){




    }

}