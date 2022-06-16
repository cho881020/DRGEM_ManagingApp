package kr.co.drgem.managingapp.menu.request.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.viewholder.RequestDetailListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData

class RequestDetailListAdapter(
    val mList : ArrayList<Pummokdetail>,
    val listener : RequestDetailEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestDetailListViewHolder -> holder.bind(mList[position], tempData)
        }
    }

    override fun getItemCount() = mList.size

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

}