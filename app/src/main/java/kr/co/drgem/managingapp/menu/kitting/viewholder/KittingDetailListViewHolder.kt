package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.models.Pummokdetail

class KittingDetailListViewHolder(parent: ViewGroup, val listener : KittingDetailEditListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.kitting_detail_list_item, parent, false)
) {

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val saying = itemView.findViewById<TextView>(R.id.saying)
    val danwi = itemView.findViewById<TextView>(R.id.danwi)
    val location = itemView.findViewById<TextView>(R.id.location)
    val hyeonjaegosuryang = itemView.findViewById<TextView>(R.id.hyeonjaegosuryang)
    val yocheongsuryang = itemView.findViewById<TextView>(R.id.yocheongsuryang)
    val gichulgosuryang = itemView.findViewById<TextView>(R.id.gichulgosuryang)
    val chulgosuryang = itemView.findViewById<EditText>(R.id.chulgosuryang)
    val jungyojajeyeobu = itemView.findViewById<TextView>(R.id.jungyojajeyeobu)


    fun bind(data : Pummokdetail) {

        itemView.setOnClickListener {
            chulgosuryang.requestFocus()
        }

        btnEdit.setOnClickListener {
            listener.onClickedEdit()
        }

        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getpummyeongHP()
        dobeon_model.text = data.getdobeon_modelHP()
        saying.text = data.getsayingHP()
        danwi.text = data.getdanwiHP()
        location.text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        yocheongsuryang.text = data.getyocheongsuryangHP()
        gichulgosuryang.text = data.getgichulgosuryangHP()
        chulgosuryang.setText(data.getchulgosuryangHP())
        jungyojajeyeobu.text = data.getjungyojajeyeobuHP()


        if(data.jungyojajeyeobu == "Y" ){
            jungyojajeyeobu.isVisible = true
            btnEdit.isVisible = true
        }
        else {
            jungyojajeyeobu.isVisible = false
            btnEdit.isVisible = false
        }

    }


}