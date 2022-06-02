package kr.co.drgem.managingapp.menu.request.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogEditRequestAdapter(
    val viewholderCount : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = viewholderCount


}