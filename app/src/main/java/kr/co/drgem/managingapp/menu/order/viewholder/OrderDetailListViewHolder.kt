package kr.co.drgem.managingapp.menu.order.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.models.Baljudetail

class OrderDetailListViewHolder(
    parent: ViewGroup,
    val listener: OrderDetailEditListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_detail_list_item, parent, false)
) {

    val txtSeq = itemView.findViewById<TextView>(R.id.txtSeq)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val saying = itemView.findViewById<TextView>(R.id.saying)
    val balhudanwi = itemView.findViewById<TextView>(R.id.balhudanwi)
    val baljusuryang = itemView.findViewById<TextView>(R.id.baljusuryang)
    val ipgoyejeongil = itemView.findViewById<TextView>(R.id.ipgoyejeongil)
    val giipgosuryang = itemView.findViewById<TextView>(R.id.giipgosuryang)
    val jungyojajeyeobu = itemView.findViewById<TextView>(R.id.jungyojajeyeobu)

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)


    fun bind(data: Baljudetail) {

        txtSeq.text = data.getSeqHP()
        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getPummyeongHP()
        dobeon_model.text = data.getDobeonModelHP()
        saying.text = data.getSayingHP()
        balhudanwi.text = data.getBalhudanwiHP()
        baljusuryang.text = data.getBaljusuryangHP()
        ipgoyejeongil.text = data.getIpgoyejeongilHP()
        giipgosuryang.text = data.getGiipgosuryangHP()
        jungyojajeyeobu.text = data.getJungyojajeyeobuHP()




        itemView.setOnClickListener {
            edtCount.requestFocus()
        }



        if (data.jungyojajeyeobu == "Y") {
            btnEdit.visibility = View.VISIBLE
            jungyojajeyeobu.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.GONE
            jungyojajeyeobu.visibility = View.GONE
        }


        btnEdit.setOnClickListener {

            val inputCount = edtCount.text.toString()
            Log.d("yj", "inputCount : $inputCount")

            try {
                val count: Int = inputCount.toInt()
                if (count >= 1) {
                    listener.onClickedEdit(count, data)
                } else {
                    Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                edtCount.text = null
                Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}