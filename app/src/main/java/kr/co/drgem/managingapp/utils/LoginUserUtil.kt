package kr.co.drgem.managingapp.utils

import kr.co.drgem.managingapp.models.BasicResponse
import kr.co.drgem.managingapp.models.Common
import kr.co.drgem.managingapp.models.MasterDataResponse

class LoginUserUtil {

    companion object {
        private var loginData : BasicResponse? = null

        fun setLoginData(data : BasicResponse) {

            if(loginData == null){
                loginData = data
            }

        }

        fun getLoginData() : BasicResponse? {

            return loginData
        }

    }


}