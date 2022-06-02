package kr.co.drgem.managingapp.roomdb.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.co.drgem.managingapp.models.Baljubeonho

@Dao
interface BaljubeonhoDao {

    @Query("SELECT * FROM Baljubeonho")
    fun getAllSavedBaljubeonhoList() : List<Baljubeonho>

    @Query("DELETE FROM Baljubeonho")
    fun deleteAllSavedBaljubeonhoList()

    @Insert
    fun insertBaljubeonhoList(baljubeonhoList: List<Baljubeonho>)
}