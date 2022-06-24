package kr.co.drgem.managingapp.menu.notdelivery.viewholder

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.menu.notdelivery.NotDeliveryEditListener
import kr.co.drgem.managingapp.models.PummokdetailDelivery
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotDeliveryListViewHolder(parent: ViewGroup, val listener: NotDeliveryEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.notdelivery_list_item, parent, false)
    ) {

    var data: PummokdetailDelivery? = null

    val yocheongil = itemView.findViewById<TextView>(R.id.yocheongil)
    val yocheongbeonho = itemView.findViewById<TextView>(R.id.yocheongbeonho)
    val yocheongchanggo = itemView.findViewById<TextView>(R.id.yocheongchanggo)
    val yocheongja = itemView.findViewById<TextView>(R.id.yocheongja)
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
    val seq = itemView.findViewById<TextView>(R.id.seq)
    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val layoutEdit = itemView.findViewById<LinearLayout>(R.id.layoutEdit)

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val serialCount = chulgosuryang.text.toString().trim()
            data?.setSerialCount(serialCount)
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

    fun bind(data: PummokdetailDelivery, tempData: TempData, position: Int) {

        this.data = data

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
        sayang.text = data.getsayangHP()
        danwi.text = data.getdanwiHP()
        location.text = data.getlocationHP()
        hyeonjaegosuryang.text = data.gethyeonjaegosuryangHP()
        yocheongsuryang.text = data.getyocheongsuryangHP()
        gichulgosuryang.text = data.getgichulgosuryangHP()
        seq.text = "${position+1}"


        if (data.jungyojajeyeobu == "Y") {
            layoutEdit.visibility = View.VISIBLE
        } else {
            layoutEdit.visibility = View.GONE
        }

        val savedSerialString =
            SerialManageUtil.getSerialStringByPummokCode(data.getpummokcodeHP())        // 품목 코드에 맞는 시리얼 가져와서


        if (savedSerialString != null) {

            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_FFFFFF))
            btnEdit.text = "*수정하기"
        } else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(itemView.context.resources.getColor(R.color.color_9A9A9A))
            btnEdit.text = "정보입력"

        }

        if(data.getSerialCount().isNullOrEmpty()){

            if(data.chulgosuryang?.isNotEmpty() == true){
                data.setSerialCount(data.getchulgosuryangHP())
            }else{
                data.setSerialCount("0")
            }
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
                    AlertDialog.Builder(itemView.context)
                        .setMessage("수량을 입력해 주세요.")
                        .setNegativeButton("확인", null)
                        .show()
                }


            } catch (e: Exception) {
                chulgosuryang.setText("0")
                AlertDialog.Builder(itemView.context)
                    .setMessage("수량을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()
            }
        }

        pummyeong.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("품명")
                .setMessage(data.getpummyeongHP())
                .setNegativeButton("확인", null)
                .show()

        }


        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        chulgosuryang.setOnFocusChangeListener { view, isFocused ->
            if (!isFocused) {

                if(data.getSerialCount() ==""){
                    data.setSerialCount("0")
                }

                val tempMap = hashMapOf(
                    "requesttype" to "08003",
                    "saeopjangcode" to tempData.saeopjangcode,
                    "changgocode" to tempData.changgocode,
                    "pummokcode" to data.getpummokcodeHP(),
                    "suryang" to data.getSerialCount(),
                    "yocheongbeonho" to data.getyocheongbeonhoHP(),
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

