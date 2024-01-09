package com.brettm.holiday.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

object Globals {
    val countryViewModel = CountryInfoViewModel()
    val holidayViewModel = HolidayListViewModel()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Globals.countryViewModel.updateContent()
                    Globals.holidayViewModel.updateContent()
                    TabScreen(Globals.countryViewModel, Globals.holidayViewModel)
                }
            }
        }
    }
}

@Composable
fun TabScreen(
    countryInfoViewModel: CountryInfoViewModelInterface,
    holidayListViewModel: HolidayListViewModelInterface
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("My Country", "Worldwide")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> CountryInfoScreen(countryInfoViewModel)
            1 -> HolidayListScreen(holidayListViewModel)
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
//        GreetingView("Hello, Android!")
    }
}
