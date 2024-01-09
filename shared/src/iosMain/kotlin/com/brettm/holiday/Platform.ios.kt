package com.brettm.holiday

import platform.UIKit.UIDevice
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.regionCode

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

class IOSLocale: DeviceLocale {
    override val countryCode: String  = NSLocale.currentLocale().regionCode!!
    override val languageCode: String = NSLocale.currentLocale().languageCode
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun getLocale(): DeviceLocale = IOSLocale()