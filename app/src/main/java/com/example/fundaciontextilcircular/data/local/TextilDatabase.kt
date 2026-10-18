package com.example.fundaciontextilcircular.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity

@Database(
    entities = [
        EmprendedoraEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TextilDatabase : RoomDatabase() {

    abstract fun emprendedoraDao(): EmprendedoraDao

    companion object {

        @Volatile
        private var INSTANCE: TextilDatabase? = null

        fun getDatabase(
            context: Context
        ): TextilDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TextilDatabase::class.java,
                    "textil_circular_db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}