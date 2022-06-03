package kr.co.drgem.managingapp.roomdb

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import kr.co.drgem.managingapp.models.Baljubeonho
import kr.co.drgem.managingapp.models.Baljudetail
import kr.co.drgem.managingapp.models.OrderDetailResponse
import kr.co.drgem.managingapp.roomdb.converters.BaljuDetailTypeConverter
import kr.co.drgem.managingapp.roomdb.daos.BaljubeonhoDao
import kr.co.drgem.managingapp.roomdb.daos.OrderDetailResponseDao

@Database(entities = [Baljubeonho::class, OrderDetailResponse::class, Baljudetail::class], version = 1,)
@TypeConverters(BaljuDetailTypeConverter::class)
abstract class DatabaseHelper: RoomDatabase() {

    abstract fun baljubeonhoDao() : BaljubeonhoDao
    abstract fun orderDetailResponseDao() : OrderDetailResponseDao


    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context) : DatabaseHelper {
            if (instance == null) {
                synchronized(DatabaseHelper::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseHelper::class.java,
                        "drgem-database"
                    ).allowMainThreadQueries() .build()
                }
            }

            return instance!!
        }
    }

}