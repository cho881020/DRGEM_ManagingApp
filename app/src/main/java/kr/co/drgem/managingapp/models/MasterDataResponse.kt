package kr.co.drgem.managingapp.models

import java.io.Serializable

data class MasterDataResponse(
    val common: ArrayList<Common>,
    val commoncount: String,
    val resultcd: String,
    val resultmsg: String
) : Serializable {

    fun getCompanyCode(): ArrayList<Detailcode>{
        val companyCode: ArrayList<Detailcode> = arrayListOf()

        common.forEach {
            if (it.gubuncode == "0000") {
                companyCode.clear()
                companyCode.addAll(it.detailcode)
            }
        }
        return companyCode
    }

    fun getGwangmyeongCode(): ArrayList<Detailcode>{
        val wareHouseCode: ArrayList<Detailcode> = arrayListOf()
        val gwangmyeongCode: ArrayList<Detailcode> = arrayListOf()

        common.forEach {
            if (it.gubuncode == "0080") {
                wareHouseCode.clear()
                wareHouseCode.addAll(it.detailcode)
            }
        }

        wareHouseCode.forEach {
            if (it.code.startsWith("1")) {
                gwangmyeongCode.add(it)
            }
        }
        return gwangmyeongCode
    }

    fun getGumiCode(): ArrayList<Detailcode>{
        val wareHouseCode: ArrayList<Detailcode> = arrayListOf()
        val gumiCode: ArrayList<Detailcode> = arrayListOf()

        common.forEach {
            if (it.gubuncode == "0080") {
                wareHouseCode.clear()
                wareHouseCode.addAll(it.detailcode)
            }
        }

        wareHouseCode.forEach {
            if (it.code.startsWith("2")) {
                gumiCode.add(it)
            }
        }
        return gumiCode
    }
}