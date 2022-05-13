package kr.co.drgem.managingapp.menu.transaction.dialog.delete

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.transaction.dialog.delete.DetailTRSerialViewHolder
import kr.co.drgem.managingapp.menu.transaction.dialog.delete.DetailTRViewHolder

class DialogDetailTranAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewTypeList: ArrayList<Int> = arrayListOf()

    init {
        setData()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> DetailTRViewHolder(parent)
            1 -> DetailTRSerialViewHolder(parent)
            else -> DetailTRSerialViewHolder(parent)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = viewTypeList.size

    override fun getItemViewType(position: Int): Int {
        return viewTypeList[position]
    }

    fun setData() {

        viewTypeList.clear()
        viewTypeList.add(0)
        viewTypeList.add(1)

    }
}