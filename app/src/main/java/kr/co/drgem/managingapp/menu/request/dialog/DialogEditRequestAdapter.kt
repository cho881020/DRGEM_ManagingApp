package kr.co.drgem.managingapp.menu.request.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.localdb.SerialLocalDB

class DialogEditRequestAdapter(
    val viewholderCount : Int,
    val mList: List<SerialLocalDB>,
    val serialList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RequestSerialListViewHolder -> {
                val data = mList[position]
                holder.bind(position, data, getSerialData(position))
            }

        }
    }

    override fun getItemCount() = viewholderCount

    private fun getSerialData(position: Int): String{

        if (serialList.isNotEmpty() && serialList.size > position) {
            return serialList[position]
        }

        return ""
    }


}