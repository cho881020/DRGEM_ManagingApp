package kr.co.drgem.managingapp.menu.stock.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.stock.viewholder.StockListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail

class StockListAdapter(
    val mList : ArrayList<Pummokdetail>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StockListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is StockListViewHolder -> holder.bind(mList[position])
        }
    }

    override fun getItemCount() = mList.size
}