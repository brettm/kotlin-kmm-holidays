package com.brettm.holiday

import de.cketti.codepoints.CodePoints

object EmojiFlag {
    fun unicode(countryCode: String?): String {
        countryCode?.also {code ->
            val upper = code.uppercase()
            val regex = "\\A[A-Z]{2}\\z".toRegex()
            if (!regex.matches(code)) { return "" }
            return code.map { it.code + 0x1F1A5 }.joinToString(""){
                CodePoints.toChars(it).concatToString()
            }
        }
        return ""
    }
}