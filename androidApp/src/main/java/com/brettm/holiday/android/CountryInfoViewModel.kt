package com.brettm.holiday.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brettm.holiday.holidayApi.HolidayApiFactory
import com.brettm.holiday.holidayApi.apis.CountryApi
import com.brettm.holiday.holidayApi.apis.PublicHolidayApi
import com.brettm.holiday.holidayApi.countryInfoUsingDeviceLocale
import com.brettm.holiday.holidayApi.nextPublicHolidaysUsingDeviceLocale
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.openapitools.client.models.CountryInfoDto
import org.openapitools.client.models.PublicHolidayV3Dto

enum class CountryInfoContentKey {
    Info, Holidays
}

// Kotlin doesn't allow private/file private type aliases, which leads to verbose
// type aliases like this!
typealias CountryInfoViewModelContent = Map<CountryInfoContentKey, Any>

interface CountryInfoViewModelInterface: ViewModelInterface<CountryInfoViewModelContent> {
    val countryInfo: CountryInfoDto?
    val holidays: List<PublicHolidayV3Dto>?
}

class CountryInfoViewModel(
    private val countryClient: CountryApi = HolidayApiFactory.createCountryApi(),
    private val holidayClient: PublicHolidayApi = HolidayApiFactory.createPublicHolidayApi(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), CountryInfoViewModelInterface {

    private val _uiState = MutableStateFlow<UIState<CountryInfoViewModelContent>>(UIState.Loading(null))
    override var uiState: StateFlow<UIState<CountryInfoViewModelContent>> = _uiState.asStateFlow()

    override val countryInfo: CountryInfoDto?
        get() {
            return uiState.value.content()?.get(CountryInfoContentKey.Info) as CountryInfoDto?
        }

    @Suppress("UNCHECKED_CAST")
    override val holidays: List<PublicHolidayV3Dto>?
        get() {
            return uiState.value.content()
                ?.get(CountryInfoContentKey.Holidays) as List<PublicHolidayV3Dto>?
        }

    override fun updateContent() {
        _uiState.value = UIState.Loading(uiState.value.content())
        viewModelScope.launch(dispatcher) {
            try {
                _uiState.value = UIState.Loaded(fetchContent())
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.toString())
            }
        }
    }

    @Throws(Exception::class)
    private suspend fun fetchContent(): CountryInfoViewModelContent {
        val content: MutableMap<CountryInfoContentKey, Any> = mutableMapOf()
        val jobs = listOf(
            viewModelScope.launch(dispatcher) {
                val countryInfoResult = fetchContent() {
                    countryClient.countryInfoUsingDeviceLocale()
                }
                content[CountryInfoContentKey.Info] = countryInfoResult.getOrThrow()
            },
            viewModelScope.launch(dispatcher) {
                val holidayResult = fetchContent() {
                    holidayClient.nextPublicHolidaysUsingDeviceLocale()
                }
                content[CountryInfoContentKey.Holidays] = holidayResult.getOrThrow()
            }
        )
        jobs.joinAll()
        return content
    }
}
