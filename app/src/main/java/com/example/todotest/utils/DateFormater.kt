package com.example.todotest.utils


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Extension function for Date
fun Date.format(pattern: String = "dd MMM yyyy, hh:mm a"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}
