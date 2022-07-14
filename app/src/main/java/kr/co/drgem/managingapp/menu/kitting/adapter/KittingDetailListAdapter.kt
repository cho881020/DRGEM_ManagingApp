/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingDetailListAdapter.kt
 * 개 발 자 : (주)디알젬
 * 업무기능 : 키팅출고 화면으로 키팅명세요청 및 일괄출고등록 기능 Adapter
 */
package kr.co.drgem.managingapp.menu.kitting.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.selects.select
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.menu.kitting.viewholder.KittingDetailListViewHolder
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData

class KittingDetailListAdapter(
    val listener : KittingDetailEditListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = ArrayList<Pummokdetail>()
    var tempData = TempData("","","","","","",)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KittingDetailListViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is KittingDetailListViewHolder -> holder.bind(mList[position], tempData, position)
        }
    }

    override fun getItemCount() = mList.size  // getItemCount()도 Adapter 의 기본 멤버함수이다. 무조건 있어야 한다??

    fun setTemp(tempData : TempData) {
        this.tempData = tempData
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Pummokdetail>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun onClickedView(position: Int){
        mList.forEachIndexed { index, pummokdetail ->

            pummokdetail.itemViewClicked = false
            if(index == position){
                pummokdetail.itemViewClicked = true
            }
        }
        notifyDataSetChanged()
    }

}