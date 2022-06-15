package kr.co.drgem.managingapp.menu.notdelivery.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.viewholder.NotDeliveryListViewHolder
import kr.co.drgem.managingapp.models.PummokdetailDelivery
import kr.co.drgem.managingapp.models.TempData

class NotDeliveryListAdapter(
    val mList : ArrayList<PummokdetailDelivery>,
    val listener : NotDeliveryEditListener,
    val tempData : TempData
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliveryListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is NotDeliveryListViewHolder -> {
                holder.bind(mList[position], tempData)
            }
        }

    }

    override fun getItemCount() = mList.size
}