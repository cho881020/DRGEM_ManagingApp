/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : StockListEditListener.kt
 * 개 발 자 : (주)디알젬
 * 업무기능 : 재고관리 수량 수정을 위한 Listener
 */
package kr.co.drgem.managingapp.menu.stock

import kr.co.drgem.managingapp.models.PummokdetailStock

interface StockListEditListener {

    fun onClickedEdit(data: PummokdetailStock)

    fun onItemViewClicked(position: Int)

}