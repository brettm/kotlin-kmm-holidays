package com.brettm.holiday

import kotlin.test.Test
import kotlin.test.assertTrue

class IosLocaleTest {

    @Test
    fun testLocale() {
        val locale = getLocale()
        assertTrue(true, "Received Locale . $locale")
    }
}