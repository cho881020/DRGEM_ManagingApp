package kr.co.drgem.managingapp.menu.tradingstatement.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.tradingstatement.EditListener

class TradingStatementListViewHolder(parent: ViewGroup, val listener: EditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.trading_statement_list_item, parent, false)
) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    fun bind() {

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }

    }

}