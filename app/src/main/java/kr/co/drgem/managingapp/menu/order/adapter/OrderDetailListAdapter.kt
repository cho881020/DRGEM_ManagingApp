package kr.co.drgem.managingapp.menu.order.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.viewholder.OrderDetailListViewHolder
import kr.co.drgem.managingapp.models.Baljudetail

class OrderDetailListAdapter(
    val listener: OrderDetailEditListener,
    val mContext: Context,
    val mList: List<Baljudetail>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderDetailListViewHolder(mContext, parent, listener)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("뷰홀더바인딩", "${position}번째 줄")
        (holder as OrderDetailListViewHolder).bind(mList[position])

    }

    override fun getItemCount() = mList.size


}