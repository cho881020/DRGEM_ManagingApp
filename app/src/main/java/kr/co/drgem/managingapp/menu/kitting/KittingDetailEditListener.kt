/**
 * 프로젝트명 : 스마트창고관리 시스템
 * 프로그램명 : KittingDetailEditListener.kt
 * 개 발 자 : (주)디알젬
 * 업무기능 : 키팅출고 화면으로 키팅명세요청 및 일괄출고등록 기능 Listener
 */
package kr.co.drgem.managingapp.menu.kitting

import kr.co.drgem.managingapp.models.Pummokdetail

interface KittingDetailEditListener {
    //    fun onClickedEdit(count: Int, data: Pummokdetail)
    fun onClickedEdit(data: Pummokdetail)

    fun onItemViewClicked(position: Int)

}