/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : LocationListViewHolder.kt
 * 개 발 자  : (주)디알젬
 * 업무기능  : 로케이션조회 화면으로 로케이션요청 및 로케이션등록 기능 ListViewHolder
 */
package kr.co.drgem.managingapp.menu.location.viewholder

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Pummokdetail

class LocationListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.location_list_item, parent, false)
) {
    lateinit var data : Pummokdetail

    val edtWarehouse      = itemView.findViewById<EditText>(R.id.edtWarehouse)
    val pummokcode        = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong         = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model      = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang            = itemView.findViewById<TextView>(R.id.sayang)
    val danwi             = itemView.findViewById<TextView>(R.id.danwi)
    val location          = itemView.findViewById<TextView>(R.id.location)
    val seq               = itemView.findViewById<TextView>(R.id.seq)
    val hyeonjaegosuryang = itemView.findViewById<TextView>(R.id.hyeonjaegosuryang)

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val txtLocation = edtWarehouse.text.toString().trim()
            data.setLocationAdd(txtLocation)
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    init {
        edtWarehouse.onFocusChangeListener = View.OnFocusChangeListener { p0, hasFocus ->
            if (hasFocus) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_E0E0E0
                    )
                )
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_FFFFFF
                    )
                )
            }
        }
    }

    fun bind(data: Pummokdetail, position: Int) {

        this.data = data

        itemView.setOnClickListener {
            edtWarehouse.requestFocus()
        }

        edtWarehouse.setText(data.getLocationAdd())
        edtWarehouse.removeTextChangedListener(textChangeListener)
        edtWarehouse.addTextChangedListener(textChangeListener)

        pummokcode       .text = data.getPummokcodeHP()
        pummyeong        .text = data.getpummyeongHP()
        dobeon_model     .text = data.getdobeon_modelHP()
        sayang           .text = data.getsayangHP()
        danwi            .text = data.getdanwiHP()
        location         .text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        seq              .text = "${position+1}"

        pummyeong.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()
        }
    }
}