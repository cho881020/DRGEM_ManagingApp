/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : LoadingStockDialogFragment
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 재고조사 준비 진행 시 로딩 다이얼로그
 */

package kr.co.drgem.managingapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.databinding.DialogKittingDetailBinding
import kr.co.drgem.managingapp.databinding.DialogLoadingBinding
import kr.co.drgem.managingapp.databinding.DialogLoadingStockBinding

class LoadingStockDialogFragment : DialogFragment() {

    lateinit var binding: DialogLoadingStockBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_loading_stock, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false) // 외부 터치 막음

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnOk.setOnClickListener {
            dismiss()
        }

    }

    fun loadingEnd() {
        binding.layoutLoadingEnd.isVisible = true
        binding.layoutLoadingIng.isVisible = false
    }


}