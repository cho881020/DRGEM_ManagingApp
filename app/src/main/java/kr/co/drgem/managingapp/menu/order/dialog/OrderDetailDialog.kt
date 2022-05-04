package kr.co.drgem.managingapp.menu.order.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.menu.order.adapter.OrderDetailListAdapter
import kr.co.drgem.managingapp.databinding.DialogOrderDetailBinding

class OrderDetailDialog : DialogFragment() {

    lateinit var binding : DialogOrderDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_order_detail, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValues()

    }


    fun setValues(){
        val mAdapter = OrderDetailListAdapter()
        binding.itemListRecyclerView.adapter = mAdapter

    }
}