package kr.co.drgem.managingapp.menu.kitting.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogEditKittingAdapter(
    val viewholderCount : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingSerialListViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = viewholderCount


}