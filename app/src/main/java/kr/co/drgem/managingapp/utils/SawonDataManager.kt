package kr.co.drgem.managingapp.utils

import kr.co.drgem.managingapp.models.MasterSawonResponse

class SawonDataManager {

    companion object {
        private var sawonDataResponse: MasterSawonResponse? = null

        fun setSawonDataList(data: MasterSawonResponse) {

            if (sawonDataResponse == null) {
                sawonDataResponse = data
            }

        }

        fun getSawonDataList(): MasterSawonResponse? {

            return sawonDataResponse
        }

    }


}