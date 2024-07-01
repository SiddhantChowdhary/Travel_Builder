package com.example.travel_builder.Models

data class OpenAiRequest(
    val model: String,
    val messages: List<Message>
)

