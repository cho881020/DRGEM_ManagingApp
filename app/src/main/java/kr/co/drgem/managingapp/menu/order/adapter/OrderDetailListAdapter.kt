package kr.co.drgem.managingapp.menu.order.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.viewholder.OrderDetailListViewHolder

class OrderDetailListAdapter(
    val listener : OrderDetailEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderDetailListViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount() = 10
}