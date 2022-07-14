/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingDetailListViewHolder.kt
 * 개 발 자 : (주)디알젬
 * 업무기능 : 키팅출고 화면으로 키팅명세요청 및 일괄출고등록 기능 ListViewHolder
 */
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

    // 수량입력 항목 버튼
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
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        dobeon_model.setOnLongClickListener{
            AlertDialog.Builder(itemView.context)
                .setTitle("도번모델")
                .setMessage(data.getdobeon_modelHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        sayang.setOnLongClickListener{
            AlertDialog.Builder(itemView.context)
                .setTitle("사양")
                .setMessage(data.getsayangHP())
                .setNegativeButton("확인", null)
                .show()

            false
        }

        // "품목코드+요청번호"에 맞는 시리얼번호 가져오기
        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode("${data.getPummokcodeHP()}/${data.getyocheongbeonhoHP()}")

        // btnEdit의 표시를 달리한다.
        if (savedSerialString != null || data.getPummokCount() != "0") {

            Log.d("yj", "getPummokCount : ${data.getPummokCount()}")

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            if(data.getjungyojajeyeobuHP() == "Y"){
                btnEdit.text = "*정보입력"
            }
            else{
                btnEdit.text = "*수량입력"
            }
        } else
        {
            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            if(data.getjungyojajeyeobuHP() == "Y"){
                btnEdit.text = "정보입력"
            }
            else{
                btnEdit.text = "수량입력"
            }
        }

//        // 선택된 데이터는 스킵하도록하는 컬러 입히기 루틴 테스트 한 것 // 2022.07.08 by jung
//        val kk = (position + 1) % 3
//        if (kk == 0) {
//            itemView.setBackgroundColor(
//                ContextCompat.getColor(
//                    itemView.context,
//                    R.color.color_455591
//                )
//            )
//        }

        // 수량입력 버튼을 클리하면 시리얼번호 입력루틴으로 가도록
        // KittingDetailEditListener 리스너 기능을 이용하여
        // KittingDetailActivity.kt의 onClickedEdit()를 호출한다.
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