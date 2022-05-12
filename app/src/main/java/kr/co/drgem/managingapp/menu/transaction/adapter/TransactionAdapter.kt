package kr.co.drgem.managingapp.menu.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.EditListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.TransactionListViewHolder

class TransactionAdapter(
    val EditListener : EditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionListViewHolder(parent, EditListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionListViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount() = 30


}

