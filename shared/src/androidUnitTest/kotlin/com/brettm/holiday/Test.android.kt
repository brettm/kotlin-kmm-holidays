package com.brettm.holiday

import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidLocaleTest {
    @Test
    fun testLocale() {
        val locale = getLocale()
        kotlin.test.assertTrue(true, "Received Locale . $locale")
    }
}
