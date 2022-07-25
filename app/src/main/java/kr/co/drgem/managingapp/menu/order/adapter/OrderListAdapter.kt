package kr.co.drgem.managingapp.menu.order.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.viewholder.OrderListViewHolder
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.MasterDataResponse

class OrderListAdapter(
    val baljuList : ArrayList<Baljubeonho>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderListViewHolder -> holder.bind(baljuList[position], position)
        }
    }

    override fun getItemCount() = baljuList.size

    fun clearList() {
        baljuList.clear()
        notifyDataSetChanged()
    }
}