package com.brettm.holiday

interface Platform {
    val name: String
}

interface DeviceLocale {
    val countryCode: String
    val languageCode: String
}

expect fun getPlatform(): Platform
expect fun getLocale(): DeviceLocale