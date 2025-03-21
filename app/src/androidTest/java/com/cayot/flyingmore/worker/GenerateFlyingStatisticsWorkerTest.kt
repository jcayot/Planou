package com.cayot.flyingmore.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import com.cayot.flyingmore.fake.worker.FakeAppWorkerProvider
import com.cayot.flyingmore.workers.GenerateFlyingStatisticsWorker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenerateFlyingStatisticsWorkerTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun generateNumberOfFlightStatisticsTest() {
        val worker = TestListenableWorkerBuilder<GenerateFlyingStatisticsWorker>(context)
            .setWorkerFactory(FakeAppWorkerProvider.fakeFactory)
    }
}
