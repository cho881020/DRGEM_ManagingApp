/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : StockListViewHolder.kt
 * 개 발 자  : (주)디알젬
 * 업무기능  : 재고조사 품목추가 상세 디스플레이용 ListViewHolder
 */
package kr.co.drgem.managingapp.menu.stock.viewholder

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.stock.StockListEditListener
import kr.co.drgem.managingapp.models.PummokdetailStock

class StockListViewHolder(parent: ViewGroup, val listener: StockListEditListener) :
    RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.stock_list_item, parent, false)
//class StockListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
//LayoutInflater.from(parent.context).inflate(R.layout.stock_list_item, parent, false)
) {
    val seqnumver         = itemView.findViewById<TextView>(R.id.seqnumber)
    val pummokcode        = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong         = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model      = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang            = itemView.findViewById<TextView>(R.id.sayang)
    val danwi             = itemView.findViewById<TextView>(R.id.danwi)
    val location          = itemView.findViewById<TextView>(R.id.location)
    val hyeonjaegosuryang = itemView.findViewById<TextView>(R.id.hyeonjaegosuryang)
    val locationadd       = itemView.findViewById<TextView>(R.id.locationadd)
    val worktime          = itemView.findViewById<TextView>(R.id.worktime)

    fun bind(data: PummokdetailStock, position: Int) {

        seqnumver        .text = data.getSeqNumHP()
        pummokcode       .text = data.getPummokcodeHP()
        pummyeong        .text = data.getpummyeongHP()
        dobeon_model     .text = data.getdobeon_modelHP()
        sayang           .text = data.getsayangHP()
        danwi            .text = data.getdanwiHP()
        location         .text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        locationadd      .text = data.getLocationaddHP()
        worktime         .text = data.getjosasiganHP()

        when {
            data.itemViewClicked -> {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_E0E0E0
                    )
                )
            }
//            data.serialCheck -> {
//                itemView.setBackgroundColor(
//                    ContextCompat.getColor(
//                        itemView.context,
//                        R.color.red
//                    )
//                )
//            }
            else -> {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_FFFFFF
                    )
                )
            }
        }

        itemView.setOnClickListener {
            listener.onItemViewClicked(position)  // 선택 행 표시
        }

        hyeonjaegosuryang.setOnClickListener {

            listener.onItemViewClicked(position)  // 선택 행 표시

            listener.onClickedEdit(data)          // 수량 수정
        }
    }
}