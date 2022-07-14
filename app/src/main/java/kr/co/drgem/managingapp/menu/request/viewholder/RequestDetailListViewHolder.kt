/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : RequestDetailListViewHolder.kt
 * 개 발 자 : (주)디알젬
 * 업무기능 : 자재출고 화면으로 요청명세요청 및 요청출고등록 기능  ListViewHolder
 */
package kr.co.drgem.managingapp.menu.request.viewholder

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.request.RequestDetailEditListener
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.utils.SerialManageUtil

class RequestDetailListViewHolder(parent: ViewGroup, val listener: RequestDetailEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.request_detail_list_item, parent, false)
    ) {

    var data: Pummokdetail? = null

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)

    val pummokcode = itemView.findViewById<TextView>(R.id.pummokcode)
    val pummyeong = itemView.findViewById<TextView>(R.id.pummyeong)
    val dobeon_model = itemView.findViewById<TextView>(R.id.dobeon_model)
    val sayang = itemView.findViewById<TextView>(R.id.sayang)
    val danwi = itemView.findViewById<TextView>(R.id.danwi)
    val location = itemView.findViewById<TextView>(R.id.location)
    val hyeonjaegosuryang = itemView.findViewById<TextView>(R.id.hyeonjaegosuryang)
    val yocheongsuryang = itemView.findViewById<TextView>(R.id.yocheongsuryang)
    val gichulgosuryang = itemView.findViewById<TextView>(R.id.gichulgosuryang)
    val seq = itemView.findViewById<TextView>(R.id.seq)
    val chulgosuryang = itemView.findViewById<TextView>(R.id.chulgosuryang)

    fun bind(data: Pummokdetail, tempData: TempData, position: Int) {

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

        pummokcode.text = data.getPummokcodeHP()
        pummyeong.text = data.getpummyeongHP()
        dobeon_model.text = data.getdobeon_modelHP()
        sayang.text = data.getsayangHP()
        danwi.text = data.getdanwiHP()
        location.text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        yocheongsuryang.text = data.getyocheongsuryangHP()
        gichulgosuryang.text = data.getgichulgosuryangHP()
        seq.text = "${position + 1}"
        chulgosuryang.text = data.getPummokCount()

        pummyeong.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        dobeon_model.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("도번모델")
                .setMessage(data.getdobeon_modelHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        sayang.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("사양")
                .setMessage(data.getsayangHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode(data.getPummokcodeHP())        // 품목 코드에 맞는 시리얼 가져와서

        if (savedSerialString != null || data.getPummokCount() != "0" ) {

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            if(data.getjungyojajeyeobuHP() == "Y"){
                btnEdit.text = "*정보입력"
            }
            else{
                btnEdit.text = "*수량입력"
            }
        } else {
            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            if(data.getjungyojajeyeobuHP() == "Y"){
                btnEdit.text = "정보입력"
            }
            else{
                btnEdit.text = "수량입력"
            }
        }

        btnEdit.setOnClickListener {
            // 여기서 "요청수량-기출고수량"이 1보다 작으면 입력할 수량 없음으로 표시하고 종료
            val sss1 = (yocheongsuryang.text.trim().toString().toInt()  // 요청수량
                    - gichulgosuryang.text.trim().toString().toInt())  // 기출고수량
            if (1 > sss1) {
                AlertDialog.Builder(itemView.context)
                    .setMessage("요청수량에서 기출고수량을 뺀 수량이 1보다 작습니다..")
                    .setNegativeButton("확인", null)
                    .show()
            } else {
                listener.onClickedEdit(data)
            }
        }
    }
}