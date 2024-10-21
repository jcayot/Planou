package com.cayot.flyingmore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cayot.flyingmore.ui.theme.PlanouTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			PlanouTheme {
				Surface(
					modifier = Modifier.fillMaxSize()
				) {
					FlyingMoreApp()
				}
			}
		}
	}
}
