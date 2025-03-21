package com.cayot.flyingmore

import android.app.Application
import androidx.work.Configuration
import com.cayot.flyingmore.data.AppContainer
import com.cayot.flyingmore.data.AppDataContainer
import com.cayot.flyingmore.workers.AppWorkerProvider

class FlyingMoreApplication : Application(), Configuration.Provider {

	lateinit var container: AppContainer

	override fun onCreate() {
		super.onCreate()
		container = AppDataContainer(this)
	}

	override val workManagerConfiguration: Configuration
		get() = Configuration.Builder().setWorkerFactory(workerFactory = AppWorkerProvider.factory).build()
}
