package kr.co.drgem.managingapp.menu.order.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail

class DialogEditOrderAdapter(
    val baljudetail: Baljudetail,
    val viewholderCount : Int,
    val mList: List<SerialLocalDB>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderSerialListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is OrderSerialListViewHolder -> {

                val data = mList[position]

                holder.bind(position, data)
            }
        }
    }

    override fun getItemCount() = viewholderCount


}