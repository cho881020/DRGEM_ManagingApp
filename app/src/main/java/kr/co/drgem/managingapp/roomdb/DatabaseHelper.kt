package kr.co.drgem.managingapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.co.drgem.managingapp.roomdb.daos.BaljuDao
import kr.co.drgem.managingapp.roomdb.datas.BaljuRoomData

@Database(entities = [BaljuRoomData::class], version = 1)
abstract class DatabaseHelper: RoomDatabase() {

    abstract fun baljuDao() : BaljuDao

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