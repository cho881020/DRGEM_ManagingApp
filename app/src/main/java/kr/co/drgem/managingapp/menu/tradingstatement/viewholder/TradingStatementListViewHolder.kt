package kr.co.drgem.managingapp.menu.tradingstatement.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.tradingstatement.EditListener
import kr.co.drgem.managingapp.models.Georaedetail

class TradingStatementListViewHolder(parent: ViewGroup, val listener: EditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.trading_statement_list_item, parent, false)
) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val btnDetail = itemView.findViewById<TextView>(R.id.btnDetail)

    fun bind(data : Georaedetail) {


        if(data.changeSerial){
            btnDetail.isVisible = false
            btnEdit.isVisible = true
        }
        else{
            btnDetail.isVisible = true
            btnEdit.isVisible = false
        }


        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }

        btnDetail.setOnClickListener {
            listener.onClickedDetail()
        }


    }

}