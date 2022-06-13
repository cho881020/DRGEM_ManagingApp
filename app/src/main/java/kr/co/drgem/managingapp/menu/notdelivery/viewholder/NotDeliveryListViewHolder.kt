package kr.co.drgem.managingapp.menu.notdelivery.viewholder

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.models.PummokdetailDelivery
import kr.co.drgem.managingapp.utils.SerialManageUtil

class NotDeliveryListViewHolder(parent: ViewGroup, val listener: NotDeliveryEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.notdelivery_list_item, parent, false)
    ) {
    val yocheongil = itemView.findViewById<TextView>(R.id.yocheongil)
    val yocheongbeonho = itemView.findViewById<TextView>(R.id.yocheongbeonho)
    val yocheongchanggo = itemView.findViewById<TextView>(R.id.yocheongchanggo)
    val yocheongja = itemView.findViewById<TextView>(R.id.yocheongja)
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
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    init {

        chulgosuryang.onFocusChangeListener = View.OnFocusChangeListener { p0, hasFocus ->
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

    fun bind(data: PummokdetailDelivery) {

        itemView.setOnClickListener {
            chulgosuryang.requestFocus()
        }

        yocheongil.text = data.getyocheongilHP()
        yocheongbeonho.text = data.getyocheongbeonhoHP()
        yocheongchanggo.text = data.getyocheongchanggoHP()
        yocheongja.text = data.getyocheongjaHP()
        pummokcode.text = data.getpummokcodeHP()
        pummyeong.text = data.getpummyeongHP()
        dobeon_model.text = data.getdobeon_modelHP()
        saying.text = data.getsayingHP()
        danwi.text = data.getdanwiHP()
        location.text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        yocheongsuryang.text = data.getyocheongsuryangHP()
        gichulgosuryang.text = data.getgichulgosuryangHP()
        chulgosuryang.setText(data.getchulgosuryangHP())


        btnEdit.isVisible = data.jungyojajeyeobu == "Y"

        val savedSerialString = SerialManageUtil.getSerialStringByPummokCode(data.getpummokcodeHP())        // 품목 코드에 맞는 시리얼 가져와서


        if (savedSerialString != null) {

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            btnEdit.text = "*수정하기"
        }
        else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            btnEdit.text = "정보입력"

        }

        if(savedSerialString != null){
            val serialData = SerialManageUtil.getSerialStringByPummokCode(data.getpummokcodeHP())?.let {
                val count = it.split(",").size.toString()

                chulgosuryang.setText(count)
            }

        }else{
            chulgosuryang.setText(data.getchulgosuryangHP())
        }




        btnEdit.setOnClickListener {

            val inputCount = chulgosuryang.text.toString()
            Log.d("yj", "inputCount : $inputCount")

            try {
                val count: Int = inputCount.toInt()
                if (count >= 1) {
                    listener.onClickedEdit(count, data)
                } else {
                    Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                chulgosuryang.text = null
                Toast.makeText(itemView.context, "수량을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        pummyeong.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()

        }


    }

}

