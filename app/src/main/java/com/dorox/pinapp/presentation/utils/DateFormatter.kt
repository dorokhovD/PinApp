package com.dorox.pinapp.presentation.utils

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit


fun Timestamp.toDateString(): String {

    // преобразование firebase времени в java время в миллисекунды
    val instant = Instant.ofEpochSecond(this.seconds, this.nanoseconds.toLong())

    val formatter = DateTimeFormatter
        .ofPattern("d MMM", Locale.forLanguageTag("ru"))
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)

}
