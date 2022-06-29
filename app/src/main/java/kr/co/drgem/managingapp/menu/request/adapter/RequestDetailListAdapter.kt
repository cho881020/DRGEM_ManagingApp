package kr.co.drgem.managingapp.menu.request.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.menu.request.viewholder.RequestDetailListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData

class RequestDetailListAdapter(
    val listener : RequestDetailEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = ArrayList<Pummokdetail>()
    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestDetailListViewHolder -> holder.bind(mList[position], tempData, position)
        }
    }

    override fun getItemCount() = mList.size

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Pummokdetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun onClickedView(position: Int){
        mList.forEachIndexed { index, pummokdetail ->

            pummokdetail.itemViewClicked = false
            if(index == position){
                pummokdetail.itemViewClicked = true
            }
        }
        notifyDataSetChanged()
    }

}