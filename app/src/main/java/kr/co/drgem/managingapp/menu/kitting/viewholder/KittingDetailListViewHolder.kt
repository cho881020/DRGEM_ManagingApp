package kr.co.drgem.managingapp.menu.kitting.viewholder

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
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
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.menu.kitting.KittingDetailEditListener
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KittingDetailListViewHolder(parent: ViewGroup, val listener: KittingDetailEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.kitting_detail_list_item, parent, false)
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
    val chulgosuryang = itemView.findViewById<EditText>(R.id.chulgosuryang)

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val serialCount = chulgosuryang.text.toString().trim()
            data?.setSerialCount(serialCount)
            Log.d("yj", "키팅뷰홀더포지션2 : ${data?.getSerialCount()}")
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }


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

    fun bind(data: Pummokdetail, tempData: TempData) {

        this.data = data

        itemView.setOnClickListener {
            chulgosuryang.requestFocus()
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



        btnEdit.isVisible = data.jungyojajeyeobu == "Y"


        pummyeong.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()

        }

        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode(data.getPummokcodeHP())        // 품목 코드에 맞는 시리얼 가져와서

        if (savedSerialString != null) {

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            btnEdit.text = "*수정하기"
        } else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            btnEdit.text = "정보입력"
        }


        if(data.chulgosuryang?.isNotEmpty() == true){
            data.setSerialCount(data.getchulgosuryangHP())
        }

        if(data.getSerialCount().isNullOrEmpty()){
            data.setSerialCount("0")
        }

        chulgosuryang.setText(data.getSerialCount())


        chulgosuryang.removeTextChangedListener(textChangeListener)
        chulgosuryang.addTextChangedListener(textChangeListener)



        if (data.serialCheck) {
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.red
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
                Log.d("yj", "키팅뷰홀더 Exception : $e")
            }
        }


        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        chulgosuryang.setOnFocusChangeListener { view, isFocused ->
            if (!isFocused) {

                val tempMap = hashMapOf(
                    "requesttype" to "08003",
                    "saeopjangcode" to tempData.saeopjangcode,
                    "changgocode" to tempData.changgocode,
                    "pummokcode" to data.getPummokcodeHP(),
                    "suryang" to data.getSerialCount(),
                    "yocheongbeonho" to tempData.yocheongbeonho,
                    "ipchulgubun" to "2",
                    "seq" to tempData.seq,
                    "tablet_ip" to IPUtil.getIpAddress(),
                    "sawoncode" to tempData.sawoncode,
                    "status" to "333",
                )

                Log.d("yj", "tempMap : $tempMap")

                apiList.postRequestTempExtantstock(tempMap).enqueue(object :
                    Callback<WorkResponse> {
                    override fun onResponse(
                        call: Call<WorkResponse>,
                        response: Response<WorkResponse>
                    ) {
                        Log.d("yj", "현재고임시등록 code : ${response.body()?.resultcd}")
                        Log.d("yj", "현재고임시등록 msg : ${response.body()?.resultmsg}")
                    }

                    override fun onFailure(call: Call<WorkResponse>, t: Throwable) {
                        Log.d("yj", "현재고임시등록")
                    }

                })

            }
        }

    }


}