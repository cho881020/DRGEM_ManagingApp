package kr.co.drgem.managingapp.menu.order.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.utils.SerialManageUtil

class OrderDetailListViewHolder(
    val mContext: Context,
    parent: ViewGroup,
    val listener: OrderDetailEditListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.order_detail_list_item, parent, false)
) {
    var data: Baljudetail? = null

    val txtSeq = itemView.findViewById<TextView>(R.id.txtSeq)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang = itemView.findViewById<TextView>(R.id.sayang)
    val balhudanwi = itemView.findViewById<TextView>(R.id.balhudanwi)
    val baljusuryang = itemView.findViewById<TextView>(R.id.baljusuryang)
    val ipgoyejeongil = itemView.findViewById<TextView>(R.id.ipgoyejeongil)
    val giipgosuryang = itemView.findViewById<TextView>(R.id.giipgosuryang)
    val location = itemView.findViewById<TextView>(R.id.location)

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val ipgosuryang = itemView.findViewById<TextView>(R.id.ipgosuryang)


    fun bind(data: Baljudetail, tempData: TempData) {

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

        txtSeq.text = data.getSeqHP()
        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getPummyeongHP()
        dobeon_model.text = data.getDobeonModelHP()
        sayang.text = data.getsayangHP()
        balhudanwi.text = data.getBalhudanwiHP()
        baljusuryang.text = data.getBaljusuryangHP()
        ipgoyejeongil.text = data.getIpgoyejeongilHP()
        giipgosuryang.text = data.getGiipgosuryangHP()
        location.text = data.getLocationHP()
        ipgosuryang.text = data.getPummokCount()


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


        val savedSerialString = SerialManageUtil.getSerialStringByPummokCode(data.getPummokcodeHP())


        if (savedSerialString != null || data.getPummokCount() != "0" ) {


            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(mContext.resources.getColor(R.color.color_FFFFFF))
            if(data.getJungyojajeyeobuHP() == "Y"){
                btnEdit.text = "*????????????"
            }
            else{
                btnEdit.text = "*????????????"
            }
        } else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(mContext.resources.getColor(R.color.color_9A9A9A))
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