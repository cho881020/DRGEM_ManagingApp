package kr.co.drgem.managingapp.menu.transaction

import kr.co.drgem.managingapp.models.Georaedetail

interface transactionEditListener {
    fun onClickedEdit(data: Georaedetail)

    fun onItemViewClicked(position: Int)
}