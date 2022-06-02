package kr.co.drgem.managingapp.menu.kitting

import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.Pummokdetail

interface KittingDetailEditListener {
    fun onClickedEdit(count : Int, data: Pummokdetail)

}