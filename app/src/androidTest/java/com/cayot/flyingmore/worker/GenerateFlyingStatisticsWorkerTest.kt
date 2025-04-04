package com.cayot.flyingmore.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.testing.TestListenableWorkerBuilder
import com.cayot.flyingmore.data.local.repository.OfflineFlightRepository
import com.cayot.flyingmore.data.local.repository.OfflineFlyingStatisticsRepository
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.repository.FlightRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.fake.worker.FakeAppWorkerProvider
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import com.cayot.flyingmore.fake.data.model.generateFakeFlight
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker
import com.cayot.flyingmore.workers.STATISTICS_TO_GENERATE_KEY
import com.cayot.flyingmore.workers.YEAR_KEY
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
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
    private lateinit var flightRepository: FlightRepository
    private lateinit var fakeWorkerFactory: WorkerFactory

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        flightRepository = OfflineFlightRepository(FakeFlightDao())
        flyingStatisticsRepository = OfflineFlyingStatisticsRepository(FakeFlyingStatisticsDao())
        fakeWorkerFactory = FakeAppWorkerProvider(flyingStatisticsRepository, flightRepository).fakeFactory
    }

    fun addFlights(number: Int) {
        runBlocking {
            val flights = generateFakeFlight(
                number = number,
                departureTimeRange = Year.of(2023).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                        to Year.of(2024).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            )
            flights.forEach { flightRepository.insertFlight(it) }
        }
    }

    @Test
    fun generateStatisticTest_numberOfFlights() {
        val inputData = Data.Builder()
            .putIntArray(STATISTICS_TO_GENERATE_KEY, intArrayOf(FlyingStatistic.NUMBER_OF_FLIGHT.ordinal))
            .putInt(YEAR_KEY, 2023).build()

        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(
            context = context,
            inputData = inputData)
            .setWorkerFactory(fakeWorkerFactory).build()

        addFlights(10)

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(1, allStatistics.size)
        }
    }

    @Test
    fun generateStatisticTest_noStatisticsKey() {
        val inputData = Data.Builder()
            .putInt(YEAR_KEY, 2023).build()

        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(
            context = context,
            inputData = inputData)
            .setWorkerFactory(fakeWorkerFactory).build()

        addFlights(10)

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(0, allStatistics.size)
        }
    }

    @Test
    fun generateStatisticTest_noYearKey() {
        val inputData = Data.Builder()
            .putIntArray(STATISTICS_TO_GENERATE_KEY, intArrayOf(FlyingStatistic.NUMBER_OF_FLIGHT.ordinal))
            .build()

        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(
            context = context,
            inputData = inputData)
            .setWorkerFactory(fakeWorkerFactory).build()

        addFlights(10)

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(0, allStatistics.size)
        }
    }

    @Test
    fun generateStatisticTest_noFlights() {
        val inputData = Data.Builder()
            .putIntArray(STATISTICS_TO_GENERATE_KEY, intArrayOf(FlyingStatistic.NUMBER_OF_FLIGHT.ordinal))
            .putInt(YEAR_KEY, 2023).build()

        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(
            context = context,
            inputData = inputData)
            .setWorkerFactory(fakeWorkerFactory).build()

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.failure(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(0, allStatistics.size)
        }
    }
}
