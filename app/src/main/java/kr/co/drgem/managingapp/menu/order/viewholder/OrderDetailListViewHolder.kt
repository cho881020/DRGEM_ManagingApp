package kr.co.drgem.managingapp.menu.order.viewholder

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
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
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.BaseActivity
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.apis.APIList
import kr.co.drgem.managingapp.apis.ServerAPI
import kr.co.drgem.managingapp.menu.order.OrderDetailEditListener
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.TempData
import kr.co.drgem.managingapp.models.WorkResponse
import kr.co.drgem.managingapp.utils.IPUtil
import kr.co.drgem.managingapp.utils.SerialManageUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    val layoutEdit = itemView.findViewById<LinearLayout>(R.id.layoutEdit)
    val edtCount = itemView.findViewById<EditText>(R.id.edtCount)

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val serialCount = edtCount.text.toString().trim()
            data?.setSerialCount(serialCount)
            Log.d("yj", "오더뷰홀더 : ${data?.getSerialCount()}")

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }


    fun bind(data: Baljudetail, tempData: TempData) {

        this.data = data

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


        itemView.setOnClickListener {
            edtCount.requestFocus()
        }


        pummyeong.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("품명")
                .setMessage(data.getPummyeongHP())
                .setNegativeButton("확인", null)
                .show()

        }


        if (data.jungyojajeyeobu == "Y") {
            layoutEdit.visibility = View.VISIBLE
        } else {
            layoutEdit.visibility = View.GONE
        }

        val savedSerialString = SerialManageUtil.getSerialStringByPummokCode(data.getPummokcodeHP())

        Log.d("품목코드", data.getPummokcodeHP())
        Log.d("품목코드", data.getPummokcodeHP())
        Log.d("불러내는 씨리얼스트링", savedSerialString.toString())
        Log.d("입고수량", data.ipgosuryang.toString())

        if (savedSerialString != null) {


            btnEdit.setBackgroundResource(R.drawable.borderbox_skyblue_round2)
            btnEdit.setTextColor(mContext.resources.getColor(R.color.color_FFFFFF))
            btnEdit.text = "*수정하기"
        }
        else {

            btnEdit.setBackgroundResource(R.drawable.btn_light_gray)
            btnEdit.setTextColor(mContext.resources.getColor(R.color.color_9A9A9A))
            btnEdit.text = "정보입력"

        }

        if(data.getSerialCount().isNullOrEmpty()){
            data.setSerialCount("0")
        }

        edtCount.setText(data.getSerialCount())

        edtCount.removeTextChangedListener(textChangeListener)
        edtCount.addTextChangedListener(textChangeListener)



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

            val inputCount = edtCount.text.toString()
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
                e.printStackTrace()
                edtCount.setText("0")
                AlertDialog.Builder(itemView.context)
                    .setMessage("수량을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .show()
                Log.d("yj", "btnEdit.setOnClickListener Exception $e")
            }
        }



        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit(itemView.context)
        apiList = retrofit.create(APIList::class.java)

        edtCount.setOnFocusChangeListener { view, isFocused ->

            if (isFocused) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_E0E0E0
                    )
                )
            }

            if (!isFocused) {

                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_FFFFFF
                    )
                )

                if(data.getSerialCount() ==""){
                    data.setSerialCount("0")
                }

                data.ipgosuryang = edtCount.text.toString()
                (mContext as BaseActivity).mSqliteDB.updateBaljuDetail(data)

                val tempMap = hashMapOf(
                    "requesttype" to "08003",
                    "saeopjangcode" to tempData.saeopjangcode,
                    "changgocode" to tempData.changgocode,
                    "pummokcode" to data.getPummokcodeHP(),
                    "suryang" to data.getSerialCount(),
                    "yocheongbeonho" to tempData.yocheongbeonho,
                    "ipchulgubun" to "1",
                    "seq" to tempData.seq,
                    "tablet_ip" to IPUtil.getIpAddress(),
                    "sawoncode" to tempData.sawoncode,
                    "status" to "333",
                )

                Log.d("yj", "tempMap : $tempMap")

                apiList.postRequestTempExtantstock(tempMap).enqueue(object : Callback<WorkResponse>{
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