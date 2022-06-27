/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : MasterDataSpinnerAdapter
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 사업장/창고 선택 스피너 어댑터
 */

package kr.co.drgem.managingapp.adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.models.Detailcode

class MasterDataSpinnerAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: ArrayList<Detailcode>
) : ArrayAdapter<Detailcode>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tmpRow = convertView
        if (tmpRow == null) {
            tmpRow = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, null)
        }

        val row = tmpRow!!

        val data = mList[position]

        val detailCode = row.findViewById<TextView>(R.id.detailCode)

        detailCode.text = data.value

        return row

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tmpRow = convertView
        if (tmpRow == null) {
            tmpRow = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, null)
        }

        val row = tmpRow!!

        val data = mList[position]

        val detailCode = row.findViewById<TextView>(R.id.detailCode)

        detailCode.text = data.value

        return row
    }

    fun setList(list: ArrayList<Detailcode>) {

        mList.clear()
        mList.addAll(list)

        notifyDataSetChanged()
    }


}