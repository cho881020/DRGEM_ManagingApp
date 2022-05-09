package kr.co.drgem.managingapp.menu.tradingstatement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.tradingstatement.SearchListener
import kr.co.drgem.managingapp.menu.tradingstatement.viewholder.*
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail

class TradingStatementAdapter(

    val listener : SearchListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewTypeList : ArrayList<Int> = arrayListOf()
    val georaedetailList : ArrayList<Georaedetail> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> TradingStatementSearchViewHolder(parent, listener)
            1 -> TradingStatementEmptyViewHolder(parent)
            2 -> TradingStatementResultViewHolder(parent)
            3 -> TradingStatementDetailViewHolder(parent)
            4 -> TradingStatementListViewHolder(parent)
            else -> TradingStatementListViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is TradingStatementSearchViewHolder -> {
                holder.bind()
            }
            is TradingStatementEmptyViewHolder -> {
                holder.bind()
            }
            is TradingStatementResultViewHolder -> {
                holder.bind()
            }
            is TradingStatementListViewHolder -> {
                holder.bind()
            }
        }

    }

    override fun getItemCount() = viewTypeList.size

    override fun getItemViewType(position: Int): Int {
        return viewTypeList[position]
    }

    fun setData( data : BasicResponse? ){
        viewTypeList.clear()

        if(data == null){
            viewTypeList.add(0)
            viewTypeList.add(1)
        }
        else{
            georaedetailList.clear()
            georaedetailList.addAll(data.georaedetail)

            viewTypeList.add(0)
            viewTypeList.add(2)
            viewTypeList.add(3)

            georaedetailList.forEach {
                viewTypeList.add(4)
            }
        }
        notifyDataSetChanged()
    }
}