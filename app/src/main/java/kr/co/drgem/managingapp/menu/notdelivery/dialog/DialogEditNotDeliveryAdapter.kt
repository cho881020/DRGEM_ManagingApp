package kr.co.drgem.managingapp.menu.notdelivery.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogEditNotDeliveryAdapter(
    val viewholderCount : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliverySerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NotDeliverySerialListViewHolder -> holder.bind(position)

        }
    }

    override fun getItemCount() = viewholderCount


}