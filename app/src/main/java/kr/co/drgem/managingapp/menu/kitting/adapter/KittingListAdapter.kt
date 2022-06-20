package kr.co.drgem.managingapp.menu.kitting.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingListViewHolder
import kr.co.drgem.managingapp.models.Kittingdetail

class KittingListAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(

) {
    val mList = ArrayList<Kittingdetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is KittingListViewHolder -> holder.bind(mList[position], position)
        }
    }

    override fun getItemCount() = mList.size

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Kittingdetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }
}