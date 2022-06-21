package kr.co.drgem.managingapp.menu.notdelivery.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.localdb.SerialLocalDB

class DialogEditNotDeliveryAdapter(
    val viewholderCount : Int,
    val mList: List<SerialLocalDB>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliverySerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NotDeliverySerialListViewHolder -> {
                val data = mList[position]
                holder.bind(position,mList)
            }
        }
    }

    override fun getItemCount() = viewholderCount


}