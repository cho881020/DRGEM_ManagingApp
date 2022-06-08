package kr.co.drgem.managingapp.menu.transaction.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R

class TranSerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_tran_serial_list, parent, false)
) {

    val txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)

    fun bind(position: Int) {
        txtNumber.text = "${position+1}"
    }

}