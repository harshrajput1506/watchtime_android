package com.app.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {

    fun formatDate(input: String): String {
        val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)

        val date = LocalDate.parse(input, parser)
        return date.format(formatter)
    }

    fun isDateInFuture(input: String): Boolean {
        val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val date = LocalDate.parse(input, parser)
        return date.isAfter(LocalDate.now())
    }

}
