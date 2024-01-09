package com.brettm.holiday
import com.brettm.holiday.holidayApi.apis.PublicHolidayApi
import org.openapitools.client.models.PublicHolidayV3Dto
import util.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PublicHolidayAPITest {

    suspend fun fetchData(): List<PublicHolidayV3Dto>? {
        val client = PublicHolidayApi(baseUrl = "https://date.nager.at")
        return try {
            val response = client.nextPublicHolidays("GB")
            if(response.success) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            print(e.toString())
            null
        }
    }
    @Test
    fun testAPIClient() = runTest {
        val response = fetchData()
        print(response)
    }
}
class EmojiFlagTest {

    @Test
    fun testEmojiFlag() {
        val flag = EmojiFlag.unicode(countryCode = "GB")
        assertTrue {
            flag == "🇬🇧"
        }
    }
}