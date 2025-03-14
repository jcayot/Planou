package com.cayot.flyingmore.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cayot.flyingmore.data.local.FlyingMoreDatabase
import org.junit.After
import org.junit.Before
import java.io.IOException

abstract class DaoTestBase {
    protected lateinit var database: FlyingMoreDatabase

    @Before
    fun createDatabase() {
        val context : Context = ApplicationProvider.getApplicationContext()

        database = Room.inMemoryDatabaseBuilder(context, FlyingMoreDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }
}