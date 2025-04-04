plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("com.google.devtools.ksp") version "2.1.10-1.0.29"
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
	alias(libs.plugins.kotlin.compose)
}

android {
	namespace = "com.cayot.flyingmore"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.cayot.flyingmore"
		minSdk = 28
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.15"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	testFixtures {
		enable = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.runtime)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.navigation.runtime.ktx)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.material.icons.extended)
	//Room
	implementation(libs.androidx.room.runtime)
	ksp(libs.androidx.room.compiler)
	implementation(libs.androidx.room.ktx)

	//Maps
	implementation (libs.maps.compose)

	//Gson
	implementation (libs.gson)

	//Work Manager
	implementation(libs.androidx.work.runtime.ktx)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)

	//Work Manager
	androidTestImplementation(libs.androidx.work.testing)

	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	testFixturesCompileOnly(libs.kotlin.stdlib)
	testFixturesImplementation(libs.androidx.core.ktx)
	testFixturesImplementation(libs.androidx.work.runtime.ktx)
	testFixturesImplementation(platform(libs.androidx.compose.bom))
	testFixturesImplementation(libs.androidx.runtime)
}
java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

secrets {
	// To add your Maps API key to this project:
	// 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
	// 2. Add this line, where YOUR_API_KEY is your API key:
	//        MAPS_API_KEY=YOUR_API_KEY
	propertiesFileName = "secrets.properties"

	// A properties file containing default secret values. This file can be
	// checked in version control.
	defaultPropertiesFileName = "local.defaults.properties"

	// Configure which keys should be ignored by the plugin by providing regular expressions.
	// "sdk.dir" is ignored by default.
	ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
	ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}