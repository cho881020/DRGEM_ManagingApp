package kr.co.drgem.managingapp.menu.tradingstatement.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.tradingstatement.SearchListener

class TradingStatementSearchViewHolder(parent : ViewGroup, val listener: SearchListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.trading_statement_search, parent, false)
) {
    val btnFind = itemView.findViewById<TextView>(R.id.btnFind)


    init {
        btnFind.setOnClickListener {
            listener.onClickedSearch()
        }
    }

    fun bind(){



    }
}