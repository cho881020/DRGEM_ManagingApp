package kr.co.drgem.managingapp.menu.location.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.location.viewholder.LocationListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail

class LocationListAdapter(
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = ArrayList<Pummokdetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LocationListViewHolder -> holder.bind(mList[position], position)
        }

    }

    override fun getItemCount() = mList.size


    fun setList(list: ArrayList<Pummokdetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

}