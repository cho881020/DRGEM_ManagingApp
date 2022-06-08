package kr.co.drgem.managingapp.utils

class SerialManageUtil {

    companion object {

        private val mHashMap = HashMap<String, String>()

        fun putSerialStringByPummokCode(pummokCode: String, content: String ) {

            mHashMap[pummokCode] = content

        }

        fun getSerialStringByPummokCode( pummokCode: String ) : String? {

            return mHashMap[pummokCode]
        }

        fun clearData() {
            mHashMap.clear()
        }

    }

}