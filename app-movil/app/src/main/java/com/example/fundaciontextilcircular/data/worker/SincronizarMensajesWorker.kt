package com.example.fundaciontextilcircular.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.repository.ChatRepository

class SincronizarMensajesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = TextilDatabase.getDatabase(applicationContext)
        val repository = ChatRepository(
            mensajeDao = database.mensajeDao(),
            appContext = applicationContext
        )

        repository.sincronizarPendientes()

        val quedanPendientes = database.mensajeDao()
            .obtenerTodosLosMensajesPendientes()
            .isNotEmpty()

        return if (quedanPendientes) Result.retry() else Result.success()
    }
}