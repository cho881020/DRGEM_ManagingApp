package kr.co.drgem.managingapp.menu.transaction.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionListViewHolder(parent: ViewGroup, val listener: transactionEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
    ) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)

    val seq = itemView.findViewById<TextView>(R.id.seq)
    val location = itemView.findViewById<TextView>(R.id.location)

    fun bind(data : Georaedetail) {

        itemView.setOnClickListener {
            edtCount.requestFocus()
        }

        btnEdit.setOnClickListener {
            Log.d("yj", "거래명세 리스트 클릭")
            listener.onClickedEdit()
        }


        seq.text = data.seq
        location.text = data.location


    }

}