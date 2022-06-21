package kr.co.drgem.managingapp.menu.transaction.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail

class DialogEditTranAdapter(
    val viewholderCount : Int,
    val mList: List<SerialLocalDB>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TranSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TranSerialListViewHolder -> {
                val data = mList[position]
                holder.bind(position,mList)
            }

        }
    }

    override fun getItemCount() = viewholderCount



}