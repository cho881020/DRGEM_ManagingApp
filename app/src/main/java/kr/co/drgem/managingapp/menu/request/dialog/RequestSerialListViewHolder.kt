package kr.co.drgem.managingapp.menu.request.dialog

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.localdb.SerialLocalDB

class RequestSerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_request_serial_list, parent, false)
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

        edtSerial.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    edtSerial.onEditorAction(5)
                    return@setOnEditorActionListener true
                }
            }

            data.serial = edtSerial.text.toString()

            return@setOnEditorActionListener actionId != 5

        }

        btnRemove.setOnClickListener {
            edtSerial.setText("")
        }
    }

}