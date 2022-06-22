package kr.co.drgem.managingapp.menu.transaction.viewholder

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
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.menu.transaction.transactionEditListener
import kr.co.drgem.managingapp.models.Georaedetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionListViewHolder(parent: ViewGroup, val listener: transactionEditListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false)
    ) {

    var data: Georaedetail? = null

    val btnEdit = itemView.findViewById<TextView>(R.id.btnEdit)
    val ipgosuryang = itemView.findViewById<EditText>(R.id.ipgosuryang)

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

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val serialCount = ipgosuryang.text.toString().trim()
            data?.setSerialCount(serialCount)
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }


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

    fun bind(data: Georaedetail, tempData: TempData) {

        this.data = data

        itemView.setOnClickListener {
            ipgosuryang.requestFocus()
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


        baljuseq.text = data.getBaljuseqHP()
        location.text = data.getLocationHP()


        if (data.jungyojajeyeobu == "Y") {
            btnEdit.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.GONE
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

        if (data.getSerialCount().isNullOrEmpty()) {

            if (data.ipgosuryang?.isNotEmpty() == true) {
                data.setSerialCount(data.getIpgosuryangHP())
            } else {
                data.setSerialCount("0")
            }
        }

        ipgosuryang.setText(data.getSerialCount())

        ipgosuryang.removeTextChangedListener(textChangeListener)
        ipgosuryang.addTextChangedListener(textChangeListener)


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

            val inputCount = ipgosuryang.text.toString()
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
                ipgosuryang.text = null
                AlertDialog.Builder(itemView.context)
                    .setMessage("수량을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()
            }
        }

        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        ipgosuryang.setOnFocusChangeListener { view, isFocused ->
            if (!isFocused) {

                val tempMap = hashMapOf(
                    "requesttype" to "08003",
                    "saeopjangcode" to tempData.saeopjangcode,
                    "changgocode" to tempData.changgocode,
                    "pummokcode" to data.getPummokcodeHP(),
                    "suryang" to data.getSerialCount(),
                    "yocheongbeonho" to data.getBaljubeonhoHP(),
                    "ipchulgubun" to "1",   //TODO - 입출구분확인
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