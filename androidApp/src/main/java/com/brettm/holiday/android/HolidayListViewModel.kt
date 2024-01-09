package com.brettm.holiday.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brettm.holiday.holidayApi.HolidayApiFactory
import com.brettm.holiday.holidayApi.PublicHolidaySorter
import com.brettm.holiday.holidayApi.apis.PublicHolidayApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.openapitools.client.models.PublicHolidayV3Dto

private typealias HolidayListViewModelContent = Map<String, List<PublicHolidayV3Dto>>

interface HolidayListViewModelInterface: ViewModelInterface<HolidayListViewModelContent> {
    val holidays: HolidayListViewModelContent?
}

class HolidayListViewModel(
    private var holidayClient: PublicHolidayApi = HolidayApiFactory.createPublicHolidayApi(),
    private var holidaySorter: PublicHolidaySorter = PublicHolidaySorter(),
    private var dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), HolidayListViewModelInterface {

    private val _uiState = MutableStateFlow<UIState<HolidayListViewModelContent>>(UIState.Loading(null))
    override var uiState: StateFlow<UIState<HolidayListViewModelContent>> = _uiState.asStateFlow()
    override val holidays: HolidayListViewModelContent?
        get() {
            return uiState.value.content()
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
    private suspend fun fetchContent(): HolidayListViewModelContent {
        var content: Map<String, List<PublicHolidayV3Dto>> = mutableMapOf()
        val job = viewModelScope.launch(dispatcher) {
            val holidays = fetchContent() {
                holidayClient.nextPublicHolidaysWorldwide()
            }
            content = holidaySorter.groupHolidays(holidays.getOrThrow())
        }
        job.join()
        return content
    }
}