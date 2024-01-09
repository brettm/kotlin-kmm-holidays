package com.brettm.holiday

import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

class AndroidLocale : DeviceLocale {
    override val countryCode:  String = Locale.getDefault().country
    override val languageCode: String = Locale.getDefault().language
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getLocale(): DeviceLocale = AndroidLocale()