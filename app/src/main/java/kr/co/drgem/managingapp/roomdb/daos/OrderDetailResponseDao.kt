package kr.co.drgem.managingapp.roomdb.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.OrderDetailResponse

@Dao
interface OrderDetailResponseDao {

    @Query("SELECT * FROM OrderDetailResponse")
    fun getAllSavedOrderDetailResponse() : OrderDetailResponse?

    @Query("DELETE FROM OrderDetailResponse")
    fun deleteAllSavedOrderDetailResponse()

    @Insert
    fun insertOrderDetailResponse(orderDetailResponse: OrderDetailResponse)
}