package kr.co.drgem.managingapp.menu.request.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingDetailListViewHolder
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingListViewHolder
import kr.co.drgem.managingapp.menu.order.viewholder.OrderListViewHolder
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.viewholder.RequestDetailListViewHolder

class RequestDetailListAdapter(
    val listener : RequestDetailEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestDetailListViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = 10
}