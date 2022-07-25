package kr.co.drgem.managingapp.menu.order.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.viewholder.OrderDetailListViewHolder
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.TempData

class OrderDetailListAdapter(
    val listener: OrderDetailEditListener,
    val mContext: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = ArrayList<Baljudetail>()
    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderDetailListViewHolder(mContext, parent, listener)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("뷰홀더바인딩", "${position}번째 줄")
        (holder as OrderDetailListViewHolder).bind(mList[position], tempData, position)

    }

    override fun getItemCount() = mList.size

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Baljudetail>) {
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