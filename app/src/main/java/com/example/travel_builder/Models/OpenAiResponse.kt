package com.example.travel_builder.Models

import com.google.gson.annotations.SerializedName

data class OpenAiResponse(
    val id: String,
    val `object`: String,
    val created: Long, // Use the correct data type based on the actual type in the JSON
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String // Use the correct data type based on the actual type in the JSON
)

data class Message(
    val role: String,
    val content: String
)

data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int
)
