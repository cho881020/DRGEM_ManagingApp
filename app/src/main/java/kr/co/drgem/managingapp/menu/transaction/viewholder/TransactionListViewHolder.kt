package kr.co.drgem.managingapp.menu.transaction.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener

class TransactionListViewHolder(parent: ViewGroup, val listenerTransaction: transactionEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
    ) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    fun bind() {


        btnEdit.setOnClickListener {
            Log.d("yj", "거래명세 리스트 클릭")
            listenerTransaction.onClickedEdit()
        }


    }

}