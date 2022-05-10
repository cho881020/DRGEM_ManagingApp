package kr.co.drgem.managingapp.menu.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.EditListener
import kr.co.drgem.managingapp.menu.transaction.SearchListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.*
import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionAdapter(

    val SearchListener : SearchListener,
    val EditListener : EditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewTypeList : ArrayList<Int> = arrayListOf()
    val georaedetailList : ArrayList<Georaedetail> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> TransactionSearchViewHolder(parent, SearchListener)
            1 -> TransactionEmptyViewHolder(parent)
            2 -> TransactionResultViewHolder(parent)
            3 -> TransactionDetailViewHolder(parent)
            4 -> TransactionListViewHolder(parent, EditListener)
            else -> TransactionListViewHolder(parent, EditListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is TransactionSearchViewHolder -> {
                holder.bind()
            }
            is TransactionEmptyViewHolder -> {
                holder.bind()
            }
            is TransactionResultViewHolder -> {
                holder.bind()
            }
            is TransactionListViewHolder -> {

                holder.bind(georaedetailList[position-3])
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