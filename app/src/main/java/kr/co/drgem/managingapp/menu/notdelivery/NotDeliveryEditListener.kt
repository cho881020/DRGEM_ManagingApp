package kr.co.drgem.managingapp.menu.notdelivery

import kr.co.drgem.managingapp.models.Pummokdetail
import kr.co.drgem.managingapp.models.PummokdetailDelivery

interface NotDeliveryEditListener {
    fun onClickedEdit(data: PummokdetailDelivery)

    fun onItemViewClicked(position: Int)
}