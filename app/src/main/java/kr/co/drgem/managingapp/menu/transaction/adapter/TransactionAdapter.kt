package kr.co.drgem.managingapp.menu.transaction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.TransactionListViewHolder
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.TempData

class TransactionAdapter(
    val transactionEditListener: transactionEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    val mList = ArrayList<Georaedetail>()
    var tempData = TempData("", "", "", "", "", "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionListViewHolder(parent, transactionEditListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionListViewHolder -> {
                holder.bind(mList[position], tempData, position)
//                Log.d("yj", "adapterTemp : $tempData")
            }
        }
    }

    override fun getItemCount() = mList.size

    fun setList(list: ArrayList<Georaedetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun setTemp(tempData: TempData) {
        this.tempData = tempData
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

