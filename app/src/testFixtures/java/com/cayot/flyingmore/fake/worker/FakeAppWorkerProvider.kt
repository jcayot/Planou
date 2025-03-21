package com.cayot.flyingmore.fake.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.cayot.flyingmore.data.local.repository.OfflineFlightsRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlyingStatisticsRepository
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker

object FakeAppWorkerProvider {
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
                        flyingStatisticsRepository = OfflineFlyingStatisticsRepository(
                            flyingStatisticsDao = FakeFlyingStatisticsDao()
                        ),
                        flightsRepository = OfflineFlightsRepository(
                            flightDao = FakeFlightDao()
                        ),
                    )
                }

                else -> null
            }
        }

    }
}