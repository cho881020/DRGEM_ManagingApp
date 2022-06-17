package kr.co.drgem.managingapp.menu.request.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.request.viewholder.RequestListViewHolder
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.Yocheongdetail

class RequestListAdapter(
    val companyCode : String,
    val wareHouseCode : String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = ArrayList<Yocheongdetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestListViewHolder -> holder.bind(mList[position], companyCode, wareHouseCode)
        }
    }

    override fun getItemCount() = mList.size

    fun setList(list: ArrayList<Yocheongdetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

}