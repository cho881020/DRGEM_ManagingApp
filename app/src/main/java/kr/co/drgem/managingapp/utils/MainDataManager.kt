package kr.co.drgem.managingapp.utils

import kr.co.drgem.managingapp.models.Common
import kr.co.drgem.managingapp.models.MasterDataResponse

class MainDataManager {

    companion object {
        private var masterData : MasterDataResponse? = null

        fun setMainData(data : MasterDataResponse) {

            if(masterData == null){
                masterData = data
            }

        }

        fun getMainData() : MasterDataResponse? {

            return masterData
        }



    }


}