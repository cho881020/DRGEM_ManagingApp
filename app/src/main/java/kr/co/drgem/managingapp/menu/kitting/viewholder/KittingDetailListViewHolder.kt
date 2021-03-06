package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.utils.SerialManageUtil

class KittingDetailListViewHolder(parent: ViewGroup, val listener: KittingDetailEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.kitting_detail_list_item, parent, false)
    ) {

    var data: Pummokdetail? = null

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    //    val layoutEdit = itemView.findViewById<LinearLayout>(R.id.layoutEdit)
    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang = itemView.findViewById<TextView>(R.id.sayang)
    val danwi = itemView.findViewById<TextView>(R.id.danwi)
    val location = itemView.findViewById<TextView>(R.id.location)
    val hyeonjaegosuryang = itemView.findViewById<TextView>(R.id.hyeonjaegosuryang)
    val yocheongsuryang = itemView.findViewById<TextView>(R.id.yocheongsuryang)
    val gichulgosuryang = itemView.findViewById<TextView>(R.id.gichulgosuryang)
    val chulgosuryang = itemView.findViewById<TextView>(R.id.chulgosuryang)
    val seq = itemView.findViewById<TextView>(R.id.seq)
    val yocheongbeonho = itemView.findViewById<TextView>(R.id.yocheongbeonho)



    fun bind(data: Pummokdetail, tempData: TempData, position: Int) {

        this.data = data

        Log.d("yj", "data : ${data.itemViewClicked}")


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


        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getpummyeongHP()
        dobeon_model.text = data.getdobeon_modelHP()
        sayang.text = data.getsayangHP()
        danwi.text = data.getdanwiHP()
        location.text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        yocheongsuryang.text = data.getyocheongsuryangHP()
        gichulgosuryang.text = data.getgichulgosuryangHP()
        yocheongbeonho.text = data.getyocheongbeonhoHP()
        seq.text = "${position + 1}"
        chulgosuryang.text = data.getPummokCount()


        pummyeong.setOnLongClickListener{
            AlertDialog.Builder(itemView.context)
                .setTitle("??????")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }

        dobeon_model.setOnLongClickListener{
            AlertDialog.Builder(itemView.context)
                .setTitle("????????????")
                .setMessage(data.getdobeon_modelHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }

        sayang.setOnLongClickListener{
            AlertDialog.Builder(itemView.context)
                .setTitle("??????")
                .setMessage(data.getsayangHP())
                .setNegativeButton("??????", null)
                .show()

            false
        }





        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode("${data.getPummokcodeHP()}/${data.getyocheongbeonhoHP()}")        // ?????? ????????? ?????? ????????? ????????????

        if (savedSerialString != null || data.getPummokCount() != "0") {

            Log.d("yj", "getPummokCount : ${data.getPummokCount()}")

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            if(data.getjungyojajeyeobuHP() == "Y"){
                btnEdit.text = "*????????????"
            }
            else{
                btnEdit.text = "*????????????"
            }
        } else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            if(data.getjungyojajeyeobuHP() == "Y"){
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