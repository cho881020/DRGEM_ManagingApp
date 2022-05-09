package kr.co.drgem.managingapp.menu.tradingstatement.dialog.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.tradingstatement.adapter.DialogTSSerialListAdapter

class DialogTSSerialViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_ts_serial, parent, false)
) {

    val addSerialRecyclerView = itemView.findViewById<RecyclerView>(R.id.addSerialRecyclerView)


    init {
        val mAdapter = DialogTSSerialListAdapter()
        addSerialRecyclerView.adapter = mAdapter
    }


    fun bind(){




    }

}