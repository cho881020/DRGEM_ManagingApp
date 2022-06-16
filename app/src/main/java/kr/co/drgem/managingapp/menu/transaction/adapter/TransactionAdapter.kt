package kr.co.drgem.managingapp.menu.transaction.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.menu.transaction.viewholder.TransactionListViewHolder
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.LoginUserUtil

class TransactionAdapter(
    val transactionEditListener : transactionEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    val mList = ArrayList<Georaedetail>()
    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionListViewHolder(parent, transactionEditListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionListViewHolder -> {
                holder.bind(mList[position], tempData)
//                Log.d("yj", "adapterTemp : $tempData")
            }
        }
    }

    override fun getItemCount() = mList.size

    fun setList (list : ArrayList<Georaedetail>){

        mList.clear()
        mList.addAll(list)

        notifyDataSetChanged()
    }

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

}

