package kr.co.drgem.managingapp.roomdb.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.co.drgem.managingapp.roomdb.datas.BaljuRoomData

@Dao
interface BaljuDao {

    @Query("SELECT * FROM BaljuRoomData WHERE baljubeonho = :baljubeonho")
    fun getBaljuListByBaljubunho(baljubeonho: String) : List<BaljuRoomData>

    @Query("DELETE FROM BaljuRoomData WHERE baljubeonho = :baljubeonho")
    fun deleteBaljuListByBaljubunho(baljubeonho: String)

    @Insert
    fun insertBaljuList(baljuList: List<BaljuRoomData>)
}