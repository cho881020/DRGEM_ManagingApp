package kr.co.drgem.managingapp.roomdb.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import kr.co.drgem.managingapp.models.Baljudetail


object BaljuDetailTypeConverter {
    @TypeConverter
    fun listToJson(value: ArrayList<Baljudetail>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Baljudetail>::class.java).toList()

}