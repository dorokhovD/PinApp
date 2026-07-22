package com.dorox.courseapp.presentation.main

fun formatDateWithMonth(date: String) : String {

   val splitDate = date.split("-")

    val month = when(splitDate[1]) {
        "01" -> "Января"
        "02" -> "Февраля"
        "03" -> "Марта"
        "04" -> "Апреля"
        "05" -> "Мая"
        "06" -> "Июня"
        "07" -> "Июля"
        "08" -> "Августа"
        "09" -> "Сентября"
        "10" -> "Октября"
        "11" -> "Ноября"
        "12" -> "Декабря"
        else -> ""
    }
    val newDate = buildString {
        append(splitDate[2])
        append(' ')
        append(month)
        append(' ')
        append(splitDate[0])
    }
    return newDate
}