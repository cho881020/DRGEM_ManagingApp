/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : StockDialogFragment
 * 개발자 : (주)NePP 이윤주
 * 업무기능 : 재고수량 등록 전 사업장코드, 창고코드 선택 다이얼로그
 */

package kr.co.drgem.managingapp.menu.stock.dialog

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.drgem.managingapp.R
import kr.co.drgem.managingapp.adapers.MasterDataSpinnerAdapter
import kr.co.drgem.managingapp.databinding.DialogStockBinding
import kr.co.drgem.managingapp.models.Detailcode
import kr.co.drgem.managingapp.utils.MainDataManager

class StockDialogFragment : DialogFragment() {

    lateinit var binding: DialogStockBinding

    var companyCode   = "0002"
    var wareHouseCode = "2001"
    var mWareHouseList: ArrayList<Detailcode> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_stock, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false) // 외부 터치 막음

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerSet()

        binding.btnSave.setOnClickListener {
            dismiss()
        }
    }

    fun spinnerSet() {

        MainDataManager.getMainData()?.let {

            val spinnerCompanyAdapter =
                MasterDataSpinnerAdapter(
                    requireContext(),
                    R.layout.spinner_list_item,
                    it.getCompanyCode()
                )
            binding.spinnerCompany.adapter = spinnerCompanyAdapter

            val spinnerWareHouseAdapter =
                MasterDataSpinnerAdapter(
                    requireContext(),
                    R.layout.spinner_list_item,
                    arrayListOf()
                )
            binding.spinnerWareHouse.adapter = spinnerWareHouseAdapter

            binding.spinnerCompany.setSelection(1)

            binding.spinnerCompany.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (it.getCompanyCode()[position].code == "0001") {
                            spinnerWareHouseAdapter.setList(it.getGwangmyeongCode())
                            companyCode = "0001"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGwangmyeongCode())
                            binding.spinnerWareHouse.setSelection(0, false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }
                        }

                        if (it.getCompanyCode()[position].code == "0002") {
                            spinnerWareHouseAdapter.setList(it.getGumiCode())
                            companyCode = "0002"

                            mWareHouseList.clear()
                            mWareHouseList.addAll(it.getGumiCode())
                            binding.spinnerWareHouse.setSelection(0, false)

                            if (mWareHouseList.size > 0) {
                                wareHouseCode = mWareHouseList[0].code
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }

            binding.spinnerWareHouse.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        wareHouseCode = mWareHouseList[position].code
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {           // 다이얼로그가 닫힐 때 메인액티비티로 전달해주는 리스너
        super.onDismiss(dialog)

        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {                        // 액티비티에 다이얼로그 리스너가 있다면,
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)       // 액티비티의 onDismiss 를 실행
        }
    }
}