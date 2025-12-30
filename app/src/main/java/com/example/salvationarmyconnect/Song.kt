package com.example.salvationarmyconnect

data class Song(
    val id: String,
    val title_marathi: String,
    val title_english: String,
    val category: String,        // e.g., "Warfare", "Praise", "Christmas"
    val eng_ref: String? = null, // e.g., "Eng. 695"
    val tune_ref: String? = null,// e.g., "Tune 167"
    val lyrics: String
)