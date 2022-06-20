package kr.co.drgem.managingapp.menu.notdelivery.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.menu.notdelivery.viewholder.NotDeliveryListViewHolder
import kr.co.drgem.managingapp.models.PummokdetailDelivery
import kr.co.drgem.managingapp.models.TempData

class NotDeliveryListAdapter(
    val listener : NotDeliveryEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val mList = ArrayList<PummokdetailDelivery>()
    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliveryListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is NotDeliveryListViewHolder -> {
                holder.bind(mList[position], tempData, position)
            }
        }
    }

    override fun getItemCount() = mList.size

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<PummokdetailDelivery>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

}