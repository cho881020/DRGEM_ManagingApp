package kr.co.drgem.managingapp.models

import java.io.Serializable

data class Common(
    val codecount: String,
    val detailcode: ArrayList<Detailcode>,
    val gubuncode: String,
    val gubunvalue: String
) : Serializable