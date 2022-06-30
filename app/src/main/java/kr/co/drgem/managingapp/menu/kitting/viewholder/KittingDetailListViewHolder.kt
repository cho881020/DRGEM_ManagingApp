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

//    val textChangeListener = object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
////            val serialCount = chulgosuryang.text.toString().trim()
////            data?.setSerialCount(serialCount)
//        }
//
//        override fun afterTextChanged(p0: Editable?) {
//
//        }
//    }


    fun bind(data: Pummokdetail, tempData: TempData, position: Int) {

        this.data = data

//        itemView.setOnClickListener {
//            chulgosuryang.requestFocus()
//        }

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

//            if (data.chulgosuryang?.isNotEmpty() == true) {
//                data.setSerialCount(data.getchulgosuryangHP())
//            } else {
            data.setPummokCount("0")
//            }
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


//        시리얼수정[뷰홀더] : 중요자재 여부 관계 없이 전부 보이기
//
//        if (data.jungyojajeyeobu == "Y") {
//            layoutEdit.visibility = View.VISIBLE
//        } else {
//            layoutEdit.visibility = View.GONE
//        }
//

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





        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode("${data.getPummokcodeHP()}/${data.getyocheongbeonhoHP()}")        // 품목 코드에 맞는 시리얼 가져와서

        if (savedSerialString != null || data.getPummokCount() != "0") {

            Log.d("yj", "getPummokCount : ${data.getPummokCount()}")

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            btnEdit.text = "*수정하기"
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








//        chulgosuryang.setText(data.getSerialCount())
//
//        chulgosuryang.removeTextChangedListener(textChangeListener)
//        chulgosuryang.addTextChangedListener(textChangeListener)


        btnEdit.setOnClickListener {

//            val inputCount = chulgosuryang.text.toString()
//            Log.d("yj", "inputCount : $inputCount")
//
//            try {
//                val count: Int = inputCount.toInt()
//                if (count >= 1) {

//                    listener.onClickedEdit(count, data)
            listener.onClickedEdit(data)
//                } else {
//                    AlertDialog.Builder(itemView.context)
//                        .setMessage("수량을 입력해 주세요.")
//                        .setNegativeButton("확인", null)
//                        .show()
//                }
//
//
//            } catch (e: Exception) {
//                chulgosuryang.setText("0")
//                AlertDialog.Builder(itemView.context)
//                    .setMessage("수량을 입력해 주세요.")
//                    .setNegativeButton("확인", null)
//                    .show()
//                Log.d("yj", "키팅뷰홀더 Exception : $e")
//            }
        }


//        val apiList: APIList
//        val retrofit = ServerAPI.getRetrofit(itemView.context)
//        apiList = retrofit.create(APIList::class.java)
//
//        chulgosuryang.setOnFocusChangeListener { view, isFocused ->
//
//            if (isFocused) {
//                itemView.setBackgroundColor(
//                    ContextCompat.getColor(
//                        itemView.context,
//                        R.color.color_E0E0E0
//                    )
//                )
//            }
//
//            if (!isFocused) {
//
//                itemView.setBackgroundColor(
//                    ContextCompat.getColor(
//                        itemView.context,
//                        R.color.color_FFFFFF
//                    )
//                )
//
//                if (data.getSerialCount() == "") {
//                    data.setSerialCount("0")
//                }
//
//                val tempMap = hashMapOf(
//                    "requesttype" to "08003",
//                    "saeopjangcode" to tempData.saeopjangcode,
//                    "changgocode" to tempData.changgocode,
//                    "pummokcode" to data.getPummokcodeHP(),
//                    "suryang" to data.getSerialCount(),
//                    "yocheongbeonho" to data.getyocheongbeonhoHP(),
//                    "ipchulgubun" to "2",
//                    "seq" to tempData.seq,
//                    "tablet_ip" to IPUtil.getIpAddress(),
//                    "sawoncode" to tempData.sawoncode,
//                    "status" to "333",
//                )
//
//                Log.d("yj", "tempMap : $tempMap")
//
//                apiList.postRequestTempExtantstock(tempMap).enqueue(object :
//                    Callback<WorkResponse> {
//                    override fun onResponse(
//                        call: Call<WorkResponse>,
//                        response: Response<WorkResponse>
//                    ) {
//                        Log.d("yj", "현재고임시등록 code : ${response.body()?.resultcd}")
//                        Log.d("yj", "현재고임시등록 msg : ${response.body()?.resultmsg}")
//                    }
//
//                    override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
//                        Log.d("yj", "현재고임시등록")
//                    }
//
//                })
//
//            }
//        }

    }


}