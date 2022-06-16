package kr.co.drgem.managingapp.menu.kitting.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingDetailListViewHolder
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingListViewHolder
import kr.co.drgem.managingapp.menu.order.viewholder.OrderListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData

class KittingDetailListAdapter(
    val mList : ArrayList<Pummokdetail>,
    val listener : KittingDetailEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is KittingDetailListViewHolder -> holder.bind(mList[position], tempData)
        }
    }

    override fun getItemCount() = mList.size

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }
}