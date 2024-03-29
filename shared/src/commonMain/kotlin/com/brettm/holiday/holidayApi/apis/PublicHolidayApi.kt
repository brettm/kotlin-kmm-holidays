/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.brettm.holiday.holidayApi.apis

import org.openapitools.client.models.PublicHolidayV3Dto

import org.openapitools.client.infrastructure.*
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*

open class PublicHolidayApi : ApiClient {

    constructor(
        baseUrl: String = ApiClient.BASE_URL,
        httpClientEngine: HttpClientEngine? = null,
        httpClientConfig: ((HttpClientConfig<*>) -> Unit)? = null,
        jsonSerializer: Json = ApiClient.JSON_DEFAULT
    ) : super(baseUrl = baseUrl, httpClientEngine = httpClientEngine, httpClientConfig = httpClientConfig, jsonBlock = jsonSerializer)

    constructor(
        baseUrl: String,
        httpClient: HttpClient
    ): super(baseUrl = baseUrl, httpClient = httpClient)

    constructor(
        baseUrl: String
    ): super(baseUrl = baseUrl, httpClientEngine = null, httpClientConfig = null, jsonBlock = ApiClient.JSON_DEFAULT)
    /**
     * Is today a public holiday
     * The calculation is made on the basis of UTC time to adjust the time please use the offset.&lt;br /&gt;  This is a special endpoint for &#x60;curl&#x60;&lt;br /&gt;&lt;br /&gt;  200 &#x3D; Today is a public holiday&lt;br /&gt;  204 &#x3D; Today is not a public holiday&lt;br /&gt;&lt;br /&gt;  &#x60;STATUSCODE&#x3D;$(curl --silent --output /dev/stderr --write-out \&quot;%{http_code}\&quot; https://date.nager.at/Api/v2/IsTodayPublicHoliday/AT)&#x60;&lt;br /&gt;&lt;br /&gt;  &#x60;if [ $STATUSCODE -ne 200 ]; then # error handling; fi&#x60;
     * @param countryCode 
     * @param countyCode  (optional)
     * @param offset utc timezone offset (optional, default to 0)
     * @return void
     */
    @Throws(Exception::class)
    open suspend fun isTodayPublicHoliday(countryCode: kotlin.String, countyCode: kotlin.String? = null, offset: kotlin.Int? = 0): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>()

        val localVariableBody = 
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        countyCode?.apply { localVariableQuery["countyCode"] = listOf("$countyCode") }
        offset?.apply { localVariableQuery["offset"] = listOf("$offset") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/api/v3/IsTodayPublicHoliday/{countryCode}".replace("{" + "countryCode" + "}", "$countryCode"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * Returns the upcoming public holidays for the next 365 days for the given country
     * 
     * @param countryCode 
     * @return kotlin.collections.List<PublicHolidayV3Dto>
     */
    @Throws(Exception::class)
    open suspend fun nextPublicHolidays(countryCode: kotlin.String): HttpResponse<List<PublicHolidayV3Dto>> {

        val localVariableAuthNames = listOf<String>()

        val localVariableBody = 
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/api/v3/NextPublicHolidays/{countryCode}".replace("{" + "countryCode" + "}", "$countryCode"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }

    @Serializable
    private class PublicHolidayNextPublicHolidaysResponse(val value: List<PublicHolidayV3Dto>) {
        companion object : KSerializer<PublicHolidayNextPublicHolidaysResponse> {
            private val serializer: KSerializer<List<PublicHolidayV3Dto>> = serializer<List<PublicHolidayV3Dto>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: PublicHolidayNextPublicHolidaysResponse) = serializer.serialize(encoder, value.value)
            override fun deserialize(decoder: Decoder) = PublicHolidayNextPublicHolidaysResponse(
                serializer.deserialize(decoder))
        }
    }

    /**
     * Returns the upcoming public holidays for the next 7 days
     * 
     * @return kotlin.collections.List<PublicHolidayV3Dto>
     */
    @Throws(Exception::class)
    open suspend fun nextPublicHolidaysWorldwide(): HttpResponse<List<PublicHolidayV3Dto>> {

        val localVariableAuthNames = listOf<String>()

        val localVariableBody = 
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/api/v3/NextPublicHolidaysWorldwide",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }

    @Serializable
    private class PublicHolidayNextPublicHolidaysWorldwideResponse(val value: List<PublicHolidayV3Dto>) {
        companion object : KSerializer<PublicHolidayNextPublicHolidaysWorldwideResponse> {
            private val serializer: KSerializer<List<PublicHolidayV3Dto>> = serializer<List<PublicHolidayV3Dto>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: PublicHolidayNextPublicHolidaysWorldwideResponse) = serializer.serialize(encoder, value.value)
            override fun deserialize(decoder: Decoder) = PublicHolidayNextPublicHolidaysWorldwideResponse(
                serializer.deserialize(decoder))
        }
    }

    /**
     * Get public holidays
     * 
     * @param year 
     * @param countryCode 
     * @return kotlin.collections.List<PublicHolidayV3Dto>
     */
    @Throws(Exception::class)
    open suspend fun publicHolidayPublicHolidaysV3(year: kotlin.Int, countryCode: kotlin.String): HttpResponse<kotlin.collections.List<PublicHolidayV3Dto>> {

        val localVariableAuthNames = listOf<String>()

        val localVariableBody = 
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/api/v3/PublicHolidays/{year}/{countryCode}".replace("{" + "year" + "}", "$year").replace("{" + "countryCode" + "}", "$countryCode"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<PublicHolidayPublicHolidaysV3Response>().map { value }
    }

    @Serializable
    private class PublicHolidayPublicHolidaysV3Response(val value: List<PublicHolidayV3Dto>) {
        companion object : KSerializer<PublicHolidayPublicHolidaysV3Response> {
            private val serializer: KSerializer<List<PublicHolidayV3Dto>> = serializer<List<PublicHolidayV3Dto>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: PublicHolidayPublicHolidaysV3Response) = serializer.serialize(encoder, value.value)
            override fun deserialize(decoder: Decoder) = PublicHolidayPublicHolidaysV3Response(
                serializer.deserialize(decoder))
        }
    }

}
