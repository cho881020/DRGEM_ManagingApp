package kr.co.drgem.managingapp.menu.order.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.menu.order.dialog.viewholder.EditOrderSerialViewHolder

class DialogEditOrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewTypeList: ArrayList<Int> = arrayListOf()

//    init {
//        setData()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EditOrderSerialViewHolder(parent)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = 7

//    override fun getItemViewType(position: Int): Int {
//        return viewTypeList[position]
//    }

//    fun setData() {
//
//        viewTypeList.clear()
//        viewTypeList.add(0)
//        viewTypeList.add(1)
//
//    }
}