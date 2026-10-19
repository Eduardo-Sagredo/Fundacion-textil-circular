package com.example.fundaciontextilcircular.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.local.dao.MensajeDao
import com.example.fundaciontextilcircular.data.local.dao.ProductoDao
import com.example.fundaciontextilcircular.data.local.entities.EmprendedoraEntity
import com.example.fundaciontextilcircular.data.local.entities.MensajeEntity
import com.example.fundaciontextilcircular.data.local.entities.ProductoEntity

@Database(
    entities = [
        EmprendedoraEntity::class,
        MensajeEntity::class,
        ProductoEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class TextilDatabase : RoomDatabase() {

    abstract fun emprendedoraDao():
            EmprendedoraDao

    abstract fun mensajeDao():
            MensajeDao

    abstract fun productoDao():
            ProductoDao


    companion object {

        @Volatile
        private var INSTANCE:
                TextilDatabase? = null


        // =========================
        // MIGRACIÓN 2 → 3
        // AGREGAR CHAT ID
        // =========================

        private val MIGRATION_2_3 =
            object : Migration(2, 3) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        ALTER TABLE mensajes
                        ADD COLUMN chatId TEXT
                        NOT NULL
                        DEFAULT 'general'
                        """.trimIndent()
                    )
                }
            }


        // =========================
        // MIGRACIÓN 3 → 4
        // AGREGAR ESTADO LEÍDO
        // =========================

        private val MIGRATION_3_4 =
            object : Migration(3, 4) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        ALTER TABLE mensajes
                        ADD COLUMN leido INTEGER
                        NOT NULL
                        DEFAULT 1
                        """.trimIndent()
                    )
                }
            }


        // =========================
        // MIGRACIÓN 4 → 5
        // CREAR TABLA PRODUCTOS
        // =========================

        private val MIGRATION_4_5 =
            object : Migration(4, 5) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS productos (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            nombre TEXT NOT NULL,
                            descripcion TEXT NOT NULL,
                            precio INTEGER NOT NULL,
                            tipo TEXT NOT NULL,
                            imagenRuta TEXT,
                            correoEmprendedora TEXT NOT NULL,
                            nombreEmprendimiento TEXT NOT NULL,
                            fechaCreacion INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                }
            }


        // =========================
        // MIGRACIÓN 5 → 6
        // AGREGAR ESTADO AL PRODUCTO
        // =========================

        private val MIGRATION_5_6 =
            object : Migration(5, 6) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        ALTER TABLE productos
                        ADD COLUMN estado TEXT
                        NOT NULL
                        DEFAULT 'DISPONIBLE'
                        """.trimIndent()
                    )
                }
            }


        fun getDatabase(
            context: Context
        ): TextilDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            TextilDatabase::class.java,
                            "textil_circular_db"
                        )
                            .addMigrations(
                                MIGRATION_2_3,
                                MIGRATION_3_4,
                                MIGRATION_4_5,
                                MIGRATION_5_6
                            )
                            .build()

                    INSTANCE =
                        instance

                    instance
                }
        }
    }
}