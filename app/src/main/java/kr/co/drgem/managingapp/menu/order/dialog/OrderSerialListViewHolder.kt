package kr.co.drgem.managingapp.menu.order.dialog

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.localdb.SerialLocalDB
import kr.co.drgem.managingapp.models.Baljudetail

class OrderSerialListViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.dialog_order_serial_list, parent, false)
) {
    var data: List<SerialLocalDB> = listOf()
    var serialPosition = -1

    val txtNumber = itemView.findViewById<TextView>(R.id.txtNumber)
    val edtSerial = itemView.findViewById<EditText>(R.id.edtSerial)
    val btnRemove = itemView.findViewById<LinearLayout>(R.id.btnRemove)

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val serial = edtSerial.text.toString()
            data[serialPosition].serial = serial
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    fun bind(position: Int, data: List<SerialLocalDB>) {

        this.data = data
        this.serialPosition = position

        txtNumber.text = "${position+1}"

        edtSerial.setText(data[position].serial)

        edtSerial.removeTextChangedListener(textChangeListener)
        edtSerial.addTextChangedListener(textChangeListener)

        if(data.size == position+1){
            edtSerial.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        edtSerial.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == 0) {
                if (keyEvent.action == KeyEvent.ACTION_UP) {

                    run loop@{
                        data.forEachIndexed { index, serialLocalDB ->

                            if (position == index) {
                                return@forEachIndexed
                            }
                            else if (edtSerial.text.toString() == data[index].serial) {

                                AlertDialog.Builder(itemView.context)
                                    .setMessage("?????? ?????? ??? ??????????????? ?????????.")
                                    .setNegativeButton("??????", null)
                                    .show()

                                edtSerial.text = null
                                edtSerial.onEditorAction(EditorInfo.IME_ACTION_DONE)

                                return@setOnEditorActionListener true
                            }
                        }
                    }

                    if (data.size == position + 1) {
                        edtSerial.onEditorAction(EditorInfo.IME_ACTION_DONE)
                        edtSerial.selectAll()
                    } else {
                        edtSerial.onEditorAction(5)
                    }

                    return@setOnEditorActionListener true
                }
            }

            return@setOnEditorActionListener actionId != 5

        }

        btnRemove.setOnClickListener {
            edtSerial.setText("")
        }

    }

}