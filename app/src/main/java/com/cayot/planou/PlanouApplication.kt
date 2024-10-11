package com.cayot.planou

import android.app.Application
import com.cayot.planou.data.AppContainer
import com.cayot.planou.data.AppDataContainer

class PlanouApplication : Application() {

	lateinit var container: AppContainer

	override fun onCreate() {
		super.onCreate()
		container = AppDataContainer(this)
	}
}
