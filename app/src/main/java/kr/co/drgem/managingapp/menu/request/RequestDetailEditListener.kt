package kr.co.drgem.managingapp.menu.request

import kr.co.drgem.managingapp.models.Pummokdetail

interface RequestDetailEditListener {
    fun onClickedEdit(data: Pummokdetail)

    fun onItemViewClicked(position: Int)

}