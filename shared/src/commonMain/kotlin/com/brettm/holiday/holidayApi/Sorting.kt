package com.brettm.holiday.holidayApi

import org.openapitools.client.models.PublicHolidayV3Dto

class PublicHolidaySorter {
    fun groupHolidays(holidays: List<PublicHolidayV3Dto>): Map<String, List<PublicHolidayV3Dto>> {
        return holidays.groupBy { it.date ?: "Unknown" }
    }
}