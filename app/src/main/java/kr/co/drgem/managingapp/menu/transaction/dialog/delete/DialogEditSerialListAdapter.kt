package kr.co.drgem.managingapp.menu.transaction.dialog.delete

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.dialog.TranSerialListViewHolder

class DialogEditSerialListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TranSerialListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is TranSerialListViewHolder ->
//                holder.bind(position)
//        }

    }

    override fun getItemCount() = 6
}