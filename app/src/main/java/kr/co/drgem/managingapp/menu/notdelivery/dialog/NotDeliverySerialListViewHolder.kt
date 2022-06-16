package kr.co.drgem.managingapp.menu.notdelivery.dialog

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.menu.order.activity.OrderDetailDetailActivity

class NotDeliverySerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_not_delivery_serial_list, parent, false)
) {
    val txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)
    val edtSerial = itemView.findViewById<EditText>(R.id.edtSerial)
    val btnRemove = itemView.findViewById<ImageView>(R.id.btnRemove)

    fun bind(position: Int, data: SerialLocalDB, serialData: String) {

        txtNumber.text = "${position+1}"

        edtSerial.setText(serialData)

        edtSerial.addTextChangedListener {

            data.serial = edtSerial.text.toString()
        }

        btnRemove.setOnClickListener {
            edtSerial.setText("")
        }


    }

}