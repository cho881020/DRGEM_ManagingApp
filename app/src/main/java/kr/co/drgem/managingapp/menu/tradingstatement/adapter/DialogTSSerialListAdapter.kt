package kr.co.drgem.managingapp.menu.tradingstatement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.tradingstatement.dialog.viewholder.EditTSSerialListViewHolder

class DialogTSSerialListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EditTSSerialListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EditTSSerialListViewHolder ->
                holder.bind()
        }

    }

    override fun getItemCount() = 6
}