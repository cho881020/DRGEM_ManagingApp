package kr.co.drgem.managingapp.menu.stock.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.stock.viewholder.StockListViewHolder
import kr.co.drgem.managingapp.models.PummokdetailStock
import kr.co.drgem.managingapp.menu.stock.StockListEditListener

class StockListAdapter(
    val listener : StockListEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mListSeq = ArrayList<PummokdetailStock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StockListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is StockListViewHolder -> holder.bind(mListSeq[position], position)
        }
    }

    override fun getItemCount() = mListSeq.size

    fun setList(list: ArrayList<PummokdetailStock>) {
        mListSeq.clear()
        mListSeq.addAll(list)

        notifyDataSetChanged()
    }

    fun onClickedView(position: Int){
        mListSeq.forEachIndexed { index, PummokdetailStock ->

            PummokdetailStock.itemViewClicked = false
            if(index == position){
                PummokdetailStock.itemViewClicked = true
            }
        }

        notifyDataSetChanged()
    }

}