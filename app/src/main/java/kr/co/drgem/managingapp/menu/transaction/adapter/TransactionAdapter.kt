package kr.co.drgem.managingapp.menu.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.TransactionListViewHolder
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.TempData

class TransactionAdapter(
    val transactionEditListener : transactionEditListener,
    val tempData : TempData
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    val mList = ArrayList<Georaedetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionListViewHolder(parent, transactionEditListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionListViewHolder -> {
                holder.bind(mList[position], tempData)
            }
        }
    }

    override fun getItemCount() = mList.size

    fun setList (list : ArrayList<Georaedetail>){

        mList.clear()
        mList.addAll(list)

        notifyDataSetChanged()
    }

}

