package com.brettm.holiday.android

sealed class UIState<out Content : Any> {
    data class Loading<Content : Any>(val content: Content?) : UIState<Content>()
    data class Loaded<Content : Any>(val content: Content) : UIState<Content>()
    data class Error(val error: String) : UIState<Nothing>()

    fun content(): Content? {
        return when (this) {
            is Loading -> content
            is Loaded -> content
            else -> null
        }
    }
}