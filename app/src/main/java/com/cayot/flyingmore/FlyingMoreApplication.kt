package com.cayot.flyingmore

import android.app.Application
import com.cayot.flyingmore.data.AppContainer
import com.cayot.flyingmore.data.AppDataContainer

class FlyingMoreApplication : Application() {

	lateinit var container: AppContainer

	override fun onCreate() {
		super.onCreate()
		container = AppDataContainer(this)
	}
}
