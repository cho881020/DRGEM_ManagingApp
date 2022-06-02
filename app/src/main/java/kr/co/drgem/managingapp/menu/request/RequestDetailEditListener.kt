package kr.co.drgem.managingapp.menu.request

import kr.co.drgem.managingapp.models.Pummokdetail

interface RequestDetailEditListener {
    fun onClickedEdit(count:Int, data: Pummokdetail)

}