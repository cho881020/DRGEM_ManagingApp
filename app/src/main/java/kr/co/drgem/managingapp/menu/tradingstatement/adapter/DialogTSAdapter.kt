package kr.co.drgem.managingapp.menu.tradingstatement.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.tradingstatement.dialog.viewholder.DialogTSDetailViewHolder
import kr.co.drgem.managingapp.menu.tradingstatement.dialog.viewholder.DialogTSSerialViewHolder

class DialogTSAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewTypeList: ArrayList<Int> = arrayListOf()

    init {
        setData()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> DialogTSDetailViewHolder(parent)
            1 -> DialogTSSerialViewHolder(parent)
            else -> DialogTSSerialViewHolder(parent)
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