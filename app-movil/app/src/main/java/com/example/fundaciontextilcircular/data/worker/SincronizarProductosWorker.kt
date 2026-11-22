package com.example.fundaciontextilcircular.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ProductoRepository

class SincronizarProductosWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = TextilDatabase.getDatabase(applicationContext)
        val repository = ProductoRepository(
            productoDao = database.productoDao(),
            appContext = applicationContext
        )

        repository.sincronizarPendientes()

        return if (database.productoDao().obtenerPendientesSincronizacion().isEmpty()) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}