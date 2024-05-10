package com.shinkevich.mailclientkotlin.ui

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
private val timeFormatter = SimpleDateFormat("HH:mm")

fun showDate(date: Date): String {
    val currentDate = Date()
    return try {
        if (dateFormatter.format(currentDate) == dateFormatter.format(date)) {
            timeFormatter.format(date)
        } else {
            dateFormatter.format(date)
        }
    } catch (e: ParseException) {
        ""
    }
}