package kr.co.drgem.managingapp.menu.kitting.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingDetailListViewHolder
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingListViewHolder
import kr.co.drgem.managingapp.menu.order.viewholder.OrderListViewHolder

class KittingDetailListAdapter(
    val listener : KittingDetailEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is KittingDetailListViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = 10
}