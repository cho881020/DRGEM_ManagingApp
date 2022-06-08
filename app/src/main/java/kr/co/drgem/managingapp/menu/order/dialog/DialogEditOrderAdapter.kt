package kr.co.drgem.managingapp.menu.order.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogEditOrderAdapter(
    val viewholderCount : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is OrderSerialListViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount() = viewholderCount


}