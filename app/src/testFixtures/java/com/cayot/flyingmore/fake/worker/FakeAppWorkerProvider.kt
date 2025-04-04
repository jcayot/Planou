package com.cayot.flyingmore.fake.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.repository.FlightRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker

class FakeAppWorkerProvider(
    flyingStatisticsRepository: FlyingStatisticsRepository,
    flightRepository: FlightRepository
) {
    val fakeFactory = object : WorkerFactory() {
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
                        flyingStatisticsRepository = flyingStatisticsRepository,
                        flightRepository = flightRepository,
                    )
                }

                else -> null
            }
        }

    }
}