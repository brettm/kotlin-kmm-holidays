package com.brettm.holiday.android

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.brettm.holiday.EmojiFlag
import org.openapitools.client.models.CountryInfoDto
import org.openapitools.client.models.PublicHolidayV3Dto

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CountryInfoScreen(viewModel: CountryInfoViewModelInterface) {

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
            viewModel.countryInfo?.also {
                item { CountryInfo(it) }
            }
            stickyHeader {
                ListHeader(title = "Upcoming Holidays")
            }
            viewModel.holidays?.also {
                items(it) { holiday ->
                    CountryHoliday(holiday)
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
fun InfoRow(title: String?, value: String?) {
    title?.also {
        value?.also { value ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(it, modifier = Modifier.weight(1.0f))
                Spacer(Modifier.weight(0.1f))
                Text(
                    value,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1.0f)
                )
            }
        }
    }
}

@Composable
fun CountryInfo(info: CountryInfoDto) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ListHeader(info.commonName + " " + EmojiFlag.unicode(info.countryCode))
        InfoRow("Official Name", info.officialName)
        InfoRow("Country Code", info.countryCode)
        InfoRow("Region", info.region)
        ListHeader("Borders Countries")
        info.borders?.onEach { border ->
            InfoRow(border.commonName, EmojiFlag.unicode(border.countryCode))
        }
    }
}

@Composable
fun CountryHoliday(holiday: PublicHolidayV3Dto) {
    Row {
        Text(holiday.date ?: "")
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = (holiday.name ?: "") + "\n(" + holiday.localName + ")")
    }
}