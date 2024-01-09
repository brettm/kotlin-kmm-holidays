package com.brettm.holiday.holidayApi

import com.brettm.holiday.getLocale
import com.brettm.holiday.holidayApi.apis.CountryApi
import com.brettm.holiday.holidayApi.apis.PublicHolidayApi

import org.openapitools.client.infrastructure.HttpResponse
import org.openapitools.client.models.CountryInfoDto
import org.openapitools.client.models.PublicHolidayV3Dto

/**
 * Returns the upcoming public holidays for the next 365 days for the given country
 * using the device locale country code
 *
 * @param countryCode
 * @return kotlin.collections.List<PublicHolidayV3Dto>
 */
@Throws(Exception::class)
suspend fun PublicHolidayApi.nextPublicHolidaysUsingDeviceLocale(): HttpResponse<List<PublicHolidayV3Dto>> {
    return this.nextPublicHolidays(getLocale().countryCode)
}

/**
 * Get country info for the given country
 * using the device locale country code
 *
 * @param countryCode
 * @return CountryInfoDto
 */
@Throws(Exception::class)
suspend fun CountryApi.countryInfoUsingDeviceLocale(): HttpResponse<CountryInfoDto> {
    return this.countryInfo(getLocale().countryCode)
}