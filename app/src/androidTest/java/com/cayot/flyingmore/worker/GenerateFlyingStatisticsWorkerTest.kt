package com.cayot.flyingmore.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.testing.TestListenableWorkerBuilder
import com.cayot.flyingmore.data.local.repository.OfflineFlightsRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlyingStatisticsRepository
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.repository.FlightsRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.fake.worker.FakeAppWorkerProvider
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import com.cayot.flyingmore.fake.data.local.model.generateFakeFlightEntity
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker
import com.cayot.flyingmore.workers.STATISTICS_TO_GENERATE_KEY
import com.cayot.flyingmore.workers.YEAR_KEY
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Year
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class GenerateFlyingStatisticsWorkerTest {
    private lateinit var context: Context
    private lateinit var flyingStatisticsRepository: FlyingStatisticsRepository
    private lateinit var flightsRepository: FlightsRepository
    private lateinit var fakeWorkerFactory: WorkerFactory

    private val inputData = Data.Builder()
        .putIntArray(STATISTICS_TO_GENERATE_KEY, intArrayOf(FlyingStatistic.NUMBER_OF_FLIGHT.ordinal))
        .putInt(YEAR_KEY, 2023).build()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        val fakeFlightDao = FakeFlightDao()
        runBlocking {
            val flights = generateFakeFlightEntity(
                number = 10,
                departureTimeRange = Year.of(2023).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                        to Year.of(2024).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            )
            flights.forEach { fakeFlightDao.insert(it) }
        }

        flightsRepository = OfflineFlightsRepository(fakeFlightDao)
        flyingStatisticsRepository = OfflineFlyingStatisticsRepository(FakeFlyingStatisticsDao())
        fakeWorkerFactory = FakeAppWorkerProvider(flyingStatisticsRepository, flightsRepository).fakeFactory
    }

    @Test
    fun generateNumberOfFlightStatisticsTest() {
        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(
            context = context,
            inputData = inputData)
            .setWorkerFactory(fakeWorkerFactory).build()
        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }
}
