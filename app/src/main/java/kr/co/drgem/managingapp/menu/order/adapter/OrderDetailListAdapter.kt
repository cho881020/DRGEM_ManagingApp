package kr.co.drgem.managingapp.menu.order.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.viewholder.OrderDetailListViewHolder
import kr.co.drgem.managingapp.models.BaljuData
import kr.co.drgem.managingapp.models.Baljudetail

class OrderDetailListAdapter(
    val listener : OrderDetailEditListener,
    val mContext: Context,
    val mList: List<Baljudetail>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderDetailListViewHolder -> {
                holder.bind(mList[position])
            }
        }
    }

    override fun getItemCount() = mList.size
}