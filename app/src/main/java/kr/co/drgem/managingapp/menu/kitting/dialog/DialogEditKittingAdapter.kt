package kr.co.drgem.managingapp.menu.kitting.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.localdb.SerialLocalDB

class DialogEditKittingAdapter(
    val viewholderCount: Int,
    val mList: List<SerialLocalDB>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KittingSerialListViewHolder -> {

                holder.bind(position, mList)

            }
        }
}

    override fun getItemCount() = viewholderCount



}