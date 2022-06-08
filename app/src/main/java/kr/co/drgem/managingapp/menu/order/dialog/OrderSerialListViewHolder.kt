package kr.co.drgem.managingapp.menu.order.dialog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail

class OrderSerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_order_serial_list, parent, false)
) {
    val txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)
    val edtSerial = itemView.findViewById<EditText>(R.id.edtSerial)

    fun bind(position: Int, data: SerialLocalDB) {
        txtNumber.text = "${position+1}"
        edtSerial.setText(data.serial)

        edtSerial.addTextChangedListener {
            data.serial = edtSerial.text.toString()
        }
    }

}