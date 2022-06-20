package kr.co.drgem.managingapp.menu.location.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.location.viewholder.LocationListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail

class LocationListAdapter(
    val mList: ArrayList<Pummokdetail>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LocationListViewHolder -> holder.bind(mList[position], position)
        }

    }

    override fun getItemCount() = mList.size


}