package kr.co.drgem.managingapp.menu.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.TransactionListViewHolder

class TransactionAdapter(
    val transactionEditListener : transactionEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionListViewHolder(parent, transactionEditListener)
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
