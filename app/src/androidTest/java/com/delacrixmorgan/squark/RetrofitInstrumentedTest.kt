package com.delacrixmorgan.squark

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delacrixmorgan.squark.api.SquarkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RetrofitInstrumentedTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().context
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    suspend fun fetchCurrencies() {
        val currencies = SquarkService.getCurrencies()
        Assert.assertTrue("Currencies Are Empty", currencies.isNotEmpty())
    }
}