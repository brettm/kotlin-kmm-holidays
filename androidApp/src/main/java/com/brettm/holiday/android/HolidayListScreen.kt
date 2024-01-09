package com.brettm.holiday.android

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brettm.holiday.EmojiFlag
import org.openapitools.client.models.PublicHolidayV3Dto

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HolidayListScreen(viewModel: HolidayListViewModelInterface) {
    val state = viewModel.uiState.collectAsState().value
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state is UIState.Loading,
        onRefresh = viewModel::updateContent
    )
    Box {
        LazyColumn(
            Modifier.pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            viewModel.holidays?.also {
                it.forEach { (date, holidays) ->
                    stickyHeader {
                        ListHeader(title = date)
                    }
                    items(holidays) { holiday ->
                        HolidayView(holiday = holiday)
                    }
                }
            }
        }
        if (state is UIState.Error) {
            RetryView("Error loading data: " + state.error) {
                viewModel.updateContent()
            }
        }
        PullRefreshIndicator(
            refreshing = state is UIState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun HolidayView(holiday: PublicHolidayV3Dto) {
    Row {
        Text(EmojiFlag.unicode(holiday.countryCode))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = (holiday.name ?: "") + "\n(" + holiday.localName + ")")
    }
}