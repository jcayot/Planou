package com.cayot.flyingmore.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
abstract class DatabaseTest {
    protected lateinit var flyingMoreDatabase: FlyingMoreDatabase

    @Before
    open fun createDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()

        flyingMoreDatabase = Room.inMemoryDatabaseBuilder(context, FlyingMoreDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        flyingMoreDatabase.close()
    }
}
