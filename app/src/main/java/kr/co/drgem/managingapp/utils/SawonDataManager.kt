package kr.co.drgem.managingapp.utils

import kr.co.drgem.managingapp.models.MasterSawonResponse

class SawonDataManager {

    companion object {
        private var sawonData: MasterSawonResponse? = null

        fun setSawonData(data: MasterSawonResponse) {

            if (sawonData == null) {
                sawonData = data
            }
        }

        fun getSawonData(): MasterSawonResponse? {
            return sawonData
        }
    }
}