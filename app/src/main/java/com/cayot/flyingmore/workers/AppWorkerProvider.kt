package com.cayot.flyingmore.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.cayot.flyingmore.FlyingMoreApplication

object AppWorkerProvider {
    val factory = object : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {
            return when (workerClassName) {
                GenerateFlyingStatisticsWorker::class.java.name -> {
                    GenerateFlyingStatisticsWorker(
                        ctx = appContext,
                        params = workerParameters,
                        flyingStatisticsRepository = appContext.flyingMoreApplication().container.flyingStatisticsRepository,
                        flightRepository = appContext.flyingMoreApplication().container.flightRepository,
                    )
                }

                else -> null
            }
        }

    }
}

fun Context.flyingMoreApplication(): FlyingMoreApplication =
    this.applicationContext as FlyingMoreApplication