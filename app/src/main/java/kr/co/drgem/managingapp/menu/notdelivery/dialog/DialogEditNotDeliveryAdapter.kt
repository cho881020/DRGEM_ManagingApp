package kr.co.drgem.managingapp.menu.notdelivery.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogEditNotDeliveryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotDeliverySerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = 7


}