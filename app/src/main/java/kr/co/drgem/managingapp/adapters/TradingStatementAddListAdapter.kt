package kr.co.drgem.managingapp.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.viewholder.TradingStatementAddListViewHolder
import kr.co.drgem.managingapp.viewholder.TradingStatementListViewHolder

class TradingStatementAddListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TradingStatementAddListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = 3
}