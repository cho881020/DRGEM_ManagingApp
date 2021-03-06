package kr.co.drgem.managingapp.menu.transaction.viewholder

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.utils.SerialManageUtil

class TransactionListViewHolder(parent: ViewGroup, val listener: transactionEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
    ) {

    var data: Georaedetail? = null

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val ipgosuryang = itemView.findViewById<TextView>(R.id.ipgosuryang)

    val seq = itemView.findViewById<TextView>(R.id.seq)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang = itemView.findViewById<TextView>(R.id.sayang)
    val balhudanwi = itemView.findViewById<TextView>(R.id.balhudanwi)
    val baljubeonho = itemView.findViewById<TextView>(R.id.baljubeonho)
    val baljusuryang = itemView.findViewById<TextView>(R.id.baljusuryang)
    val giipgosuryang = itemView.findViewById<TextView>(R.id.giipgosuryang)
    val baljuseq = itemView.findViewById<TextView>(R.id.baljuseq)
    val location = itemView.findViewById<TextView>(R.id.location)


    fun bind(data: Georaedetail, tempData: TempData, position: Int) {

        this.data = data

        when {
            data.itemViewClicked -> {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_E0E0E0
                    )
                )
            }
            data.serialCheck -> {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )
            }
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
            listener.onItemViewClicked(position)
        }

        if (data.getPummokCount().isNullOrEmpty()) {
            data.setPummokCount("0")

        }

        seq.text = data.getSeqHP()
        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getPummyeongHP()
        dobeon_model.text = data.getDobeonModelHP()
        sayang.text = data.getsayangHP()
        balhudanwi.text = data.getBalhudanwiHP()
        baljubeonho.text = data.getBaljubeonhoHP()
        baljusuryang.text = data.getBaljusuryangHP()
        giipgosuryang.text = data.getGiipgosuryangHP()
        ipgosuryang.text = data.getPummokCount()


        baljuseq.text = data.getBaljuseqHP()
        location.text = data.getLocationHP()


        pummyeong.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("??????")
                .setMessage(data.getPummyeongHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }

        dobeon_model.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("????????????")
                .setMessage(data.getDobeonModelHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }

        sayang.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("??????")
                .setMessage(data.getsayangHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }


        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode(data.getPummokcodeHP())        // ?????? ????????? ?????? ????????? ????????????


        if (savedSerialString != null || data.getPummokCount() != "0" ) {

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            if(data.getJungyojajeyeobuHP() == "Y"){
                btnEdit.text = "*????????????"
            }
            else{
                btnEdit.text = "*????????????"
            }

        } else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            if(data.getJungyojajeyeobuHP() == "Y"){
                btnEdit.text = "????????????"
            }
            else{
                btnEdit.text = "????????????"
            }

        }


        btnEdit.setOnClickListener {


            listener.onClickedEdit(data)
        }
    }

}