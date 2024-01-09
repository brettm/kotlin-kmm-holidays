package com.brettm.holiday.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.openapitools.client.infrastructure.HttpResponse

interface ViewModelInterface<T: Any> {
    var uiState: StateFlow<UIState<T>>
    fun updateContent()
}

@Throws(Exception::class)
suspend inline fun <T : Any> ViewModel.fetchContent(
    clientResponse: () -> HttpResponse<T>
): Result<T> {
    val response = clientResponse()
    return if (response.success) {
        Result.success(response.body())
    } else {
        Result.failure(Exception("Server returned " + response.status.toString()))
    }
}