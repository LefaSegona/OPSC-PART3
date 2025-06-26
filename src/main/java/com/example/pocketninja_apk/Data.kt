package com.example.pocketninja_apk

class Data {
    // Add this class to your project (e.g., `Data.kt`)
    data class Expense(
        val category: String = "",
        val description: String = "",
        val amount: Double = 0.0,
        val date: String = "",
        val imageBase64: String = ""  // Base64-encoded image string
    )
}
