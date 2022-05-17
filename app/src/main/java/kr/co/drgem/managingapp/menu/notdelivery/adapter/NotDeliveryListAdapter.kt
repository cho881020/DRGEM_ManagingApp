package kr.co.drgem.managingapp.menu.notdelivery.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.location.viewholder.LocationListViewHolder
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.viewholder.NotDeliveryListViewHolder
import kr.co.drgem.managingapp.menu.order.viewholder.OrderDetailListViewHolder

class NotDeliveryListAdapter(
    val listener : NotDeliveryEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliveryListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is NotDeliveryListViewHolder -> {
                holder.bind()
            }
        }

    }

    override fun getItemCount() = 3
}