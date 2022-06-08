package kr.co.drgem.managingapp.menu.transaction.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.Georaedetail

class TransactionListViewHolder(parent: ViewGroup, val listener: transactionEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
    ) {
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    val seq = itemView.findViewById<TextView>(R.id.seq)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val saying = itemView.findViewById<TextView>(R.id.saying)
    val balhudanwi = itemView.findViewById<TextView>(R.id.balhudanwi)
    val baljubeonho = itemView.findViewById<TextView>(R.id.baljubeonho)
    val baljusuryang = itemView.findViewById<TextView>(R.id.baljusuryang)
    val giipgosuryang = itemView.findViewById<TextView>(R.id.giipgosuryang)
    val ipgosuryang = itemView.findViewById<EditText>(R.id.ipgosuryang)
    val baljuseq = itemView.findViewById<TextView>(R.id.baljuseq)
    val jungyojajeyeobu = itemView.findViewById<TextView>(R.id.jungyojajeyeobu)
    val location = itemView.findViewById<TextView>(R.id.location)


    init {

        ipgosuryang.onFocusChangeListener = View.OnFocusChangeListener { p0, hasFocus ->
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

    fun bind(data : Georaedetail) {

        itemView.setOnClickListener {
            ipgosuryang.requestFocus()
        }


        seq.text = data.getSeqHP()
        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getPummyeongHP()
        dobeon_model.text = data.getDobeonModelHP()
        saying.text = data.getSayingHP()
        balhudanwi.text = data.getBalhudanwiHP()
        baljubeonho.text = data.getBaljubeonhoHP()
        baljusuryang.text = data.getBaljusuryangHP()
        giipgosuryang.text = data.getGiipgosuryangHP()

        ipgosuryang.setText(data.getIpgosuryangHP())

        baljuseq.text = data.getBaljuseqHP()
        jungyojajeyeobu.text = data.getJungyojajeyeobuHP()
        location.text = data.getLocationHP()


        if (data.jungyojajeyeobu == "Y") {
            btnEdit.visibility = View.VISIBLE
            jungyojajeyeobu.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.GONE
            jungyojajeyeobu.visibility = View.GONE
        }

        btnEdit.setOnClickListener {

            val inputCount = ipgosuryang.text.toString()
            Log.d("yj", "inputCount : $inputCount")

            try {
                val count: Int = inputCount.toInt()
                if (count >= 1) {
                    listener.onClickedEdit(count, data)
                } else {
                    Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                ipgosuryang.text = null
                Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}