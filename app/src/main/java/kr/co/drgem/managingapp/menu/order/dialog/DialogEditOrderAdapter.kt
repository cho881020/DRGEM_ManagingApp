package kr.co.drgem.managingapp.menu.order.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.models.Baljudetail

class DialogEditOrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewholderCount = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is OrderSerialListViewHolder -> holder.bind()
        }
    }

    override fun getItemCount() = viewholderCount


    fun listCount(count : Int) {
        viewholderCount = count
    }
}