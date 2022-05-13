package kr.co.drgem.managingapp.menu.request.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingDetailListViewHolder
import kr.co.drgem.managingapp.menu.request.viewholder.RequestListViewHolder

class RequestListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestListViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = 10
}