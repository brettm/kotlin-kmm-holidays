package com.brettm.holiday.holidayApi

import com.brettm.holiday.holidayApi.apis.CountryApi
import com.brettm.holiday.holidayApi.apis.PublicHolidayApi

object HolidayApiConstants {
    const val BASE_URL = "https://date.nager.at"
}

open class HolidayApiFactory {
    //
    val shared = HolidayApiFactory
    companion object {
        fun createPublicHolidayApi(): PublicHolidayApi {
            return PublicHolidayApi(baseUrl = HolidayApiConstants.BASE_URL)
        }
        fun createCountryApi(): CountryApi {
            return CountryApi(baseUrl = HolidayApiConstants.BASE_URL)
        }
    }
}