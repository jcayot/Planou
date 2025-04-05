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
import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import com.cayot.flyingmore.data.repository.FlightRepository
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.fake.data.local.dao.FakeFlightDao
import com.cayot.flyingmore.fake.data.local.dao.FakeFlyingStatisticsDao
import com.cayot.flyingmore.fake.data.model.generateFakeFlight
import com.cayot.flyingmore.fake.worker.FakeAppWorkerProvider
import com.cayot.flyingmore.workers.AddFlightToFlyingStatisticsWorker
import com.cayot.flyingmore.workers.NEW_FLIGHT_KEY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class AddFlightToFlyingStatisticsWorkerTest {

    private lateinit var context: Context
    private lateinit var flyingStatisticsRepository: FlyingStatisticsRepository
    private lateinit var flightRepository: FlightRepository
    private lateinit var fakeWorkerFactory: WorkerFactory

    private val timeFrameStart = LocalDate.of(2023, 1, 1)
    private val timeFrameEnd = LocalDate.of(2024, 1, 1)
    private val flight = generateFakeFlight(
        number = 1,
        departureTimeRange = timeFrameStart.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                to timeFrameEnd.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    ).first()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        flightRepository = OfflineFlightRepository(FakeFlightDao())
        flyingStatisticsRepository = OfflineFlyingStatisticsRepository(FakeFlyingStatisticsDao())
        fakeWorkerFactory = FakeAppWorkerProvider(flyingStatisticsRepository, flightRepository).fakeFactory
    }


    @Test
    fun addFlightToFlyingStatisticsTest_numberOfFlights() {
        val statistic = NumberDailyTemporalStatistic(
            timeFrameStart = timeFrameStart,
            timeFrameEnd = timeFrameEnd,
            data = List(
                ChronoUnit.DAYS.between(timeFrameStart, timeFrameEnd).toInt(),
                { 1 }
            ),
            statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
        )

        val worker = TestListenableWorkerBuilder<AddFlightToFlyingStatisticsWorker>(
            context = context,
            inputData = Data.Builder().putInt(NEW_FLIGHT_KEY, 1).build()
        ).setWorkerFactory(fakeWorkerFactory).build()

        runBlocking {
            flyingStatisticsRepository.insertFlyingStatistic(statistic)
            flightRepository.insertFlight(flight)

            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(FlyingStatistic.entries.size, allStatistics.size)

            val numberOfFlightsStatistic = allStatistics.find {
                it.statisticType == FlyingStatistic.NUMBER_OF_FLIGHT
            }

            val dayOfFlight = flight.departureTime.toInstant().atZone(ZoneOffset.UTC).toLocalDate().dayOfYear
            assertEquals(2, numberOfFlightsStatistic!!.data[dayOfFlight - 1])
            assertEquals(statistic.data.size, ChronoUnit.DAYS.between(timeFrameStart, timeFrameEnd).toInt())
            assertEquals(statistic.timeFrameStart, timeFrameStart)
            assertEquals(statistic.timeFrameEnd, timeFrameEnd)
        }
    }

    @Test
    fun addFlightToFlyingStatisticsTest_createMissingStatistics() {
        val worker = TestListenableWorkerBuilder<AddFlightToFlyingStatisticsWorker>(
            context = context,
            inputData = Data.Builder().putInt(NEW_FLIGHT_KEY, 1).build()
        ).setWorkerFactory(fakeWorkerFactory).build()

        runBlocking {
            flightRepository.insertFlight(flight)

            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)

            val allStatistics = flyingStatisticsRepository.getAllFlyingStatistics().first()
            assertEquals(FlyingStatistic.entries.size, allStatistics.size)

            val foundStatisticsType = allStatistics.map { it.statisticType }.toSet()
            assertEquals(FlyingStatistic.entries.toSet(), foundStatisticsType)

            val dayOfFlight = flight.departureTime.toInstant().atZone(ZoneOffset.UTC).toLocalDate().dayOfYear

            for (statistic in allStatistics) {
                if (statistic.statisticType.dataType == ListDataType.INT)
                    assertNotEquals(0, statistic.data[dayOfFlight - 1])
                else
                    assertNotEquals(emptyMap<String, Int>(), statistic.data[dayOfFlight - 1])

                assertEquals(statistic.data.size, ChronoUnit.DAYS.between(timeFrameStart, timeFrameEnd).toInt())
                assertEquals(statistic.timeFrameStart, timeFrameStart)
                assertEquals(statistic.timeFrameEnd, timeFrameEnd)
            }
        }
    }
}