package kr.co.drgem.managingapp.menu.order

import kr.co.drgem.managingapp.models.Baljudetail

interface OrderDetailEditListener {
    fun onClickedEdit(count : Int, data: Baljudetail)

}