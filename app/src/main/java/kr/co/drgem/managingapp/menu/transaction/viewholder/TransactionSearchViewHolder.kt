package kr.co.drgem.managingapp.menu.transaction.viewholder

import android.app.DatePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.transaction.SearchListener
import java.text.SimpleDateFormat
import java.util.*

class TransactionSearchViewHolder(parent : ViewGroup, val listener: SearchListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.transaction_search, parent, false)
) {
    val btnFind = itemView.findViewById<TextView>(R.id.btnFind)
    val btnReset = itemView.findViewById<TextView>(R.id.btnReset)
    val txtDateStart = itemView.findViewById<TextView>(R.id.txtDateStart)
    val txtDateEnd = itemView.findViewById<TextView>(R.id.txtDateEnd)
    val layoutDateStart = itemView.findViewById<LinearLayout>(R.id.layoutDateStart)
    val layoutDateEnd = itemView.findViewById<LinearLayout>(R.id.layoutDateEnd)


    init {

        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        txtDateStart.text = dateFormat.format(cal.time)
        txtDateEnd.text = dateFormat.format(cal.time)

    }

    fun bind(){

        val cal = Calendar.getInstance()
        val dateSet = SimpleDateFormat("yyyyMMdd")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        var calStart = ""
        var calEnd = ""

        layoutDateStart.setOnClickListener {

            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

                    calStart = dateSet.format(cal.time)
                    txtDateStart.text = dateFormat.format(cal.time)

                }
            }

            val datePick = DatePickerDialog(
                itemView.context,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        layoutDateEnd.setOnClickListener {
            val date = object  : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    cal.set(year,month,dayOfMonth)

                    calEnd = dateSet.format(cal.time)
                    txtDateEnd.text = dateFormat.format(cal.time)
                }
            }

            val datePick = DatePickerDialog(
                itemView.context,
                date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        btnFind.setOnClickListener {

            listener.onClickedSearch(calStart,calEnd)
            Log.d("yj", "calStart : $calStart , calEnd : $calEnd")

        }

        btnReset.setOnClickListener {


        }



    }
}