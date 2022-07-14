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

        // 전체를 clear??
        fun clearData() {
            mHashMap.clear()
        }

        // 해당 pummokCode의 데이터를 Clear
        fun clearData1( pummokCode: String ) {
            mHashMap[pummokCode] = ""
        }

        // 위의 두가지를 테스트 해보아야 한다.

    }

}