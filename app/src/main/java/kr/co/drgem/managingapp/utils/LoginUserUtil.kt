package kr.co.drgem.managingapp.utils

import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Common
import kr.co.drgem.managingapp.models.MasterDataResponse

class LoginUserUtil {

    companion object {
        private var loginData : BasicResponse? = null

        fun setLoginData(data : BasicResponse) {
            loginData = data  // 2022.07.20 by jung 아래의 문장은 이전에 로그인한 사람이 계속 남아 있도록 만든다.
            // 로그아웃시 이것을 클리어 해야하는 데 그것은 번거롭고 무조건 다시 세트시키는 것이 좋다.
//            if(loginData == null){
//                loginData = data
//            }
        }

        fun getLoginData() : BasicResponse? {
            return loginData
        }
    }
}