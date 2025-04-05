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
            val flyingStatisticsRepository = appContext.flyingMoreApplication().container.flyingStatisticsRepository
            val flightRepository = appContext.flyingMoreApplication().container.flightRepository

            return when (workerClassName) {
                GenerateFlyingStatisticsWorker::class.java.name -> {
                    GenerateFlyingStatisticsWorker(
                        ctx = appContext,
                        params = workerParameters,
                        flyingStatisticsRepository = flyingStatisticsRepository,
                        flightRepository = flightRepository,
                    )
                }
                AddFlightToFlyingStatisticsWorker::class.java.name -> {
                    AddFlightToFlyingStatisticsWorker(
                        ctx = appContext,
                        params = workerParameters,
                        flyingStatisticsRepository = flyingStatisticsRepository,
                        flightRepository = flightRepository,
                    )
                }
                RemoveFlightFromFlyingStatisticsWorker::class.java.name -> {
                    RemoveFlightFromFlyingStatisticsWorker(
                        ctx = appContext,
                        params = workerParameters,
                        flyingStatisticsRepository = flyingStatisticsRepository,
                        flightRepository = flightRepository
                    )
                }

                else -> null
            }
        }

    }
}

fun Context.flyingMoreApplication(): FlyingMoreApplication =
    this.applicationContext as FlyingMoreApplication