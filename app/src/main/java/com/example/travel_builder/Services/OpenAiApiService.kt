package com.example.travel_builder.Services

import com.example.travel_builder.Models.OpenAiRequest
import com.example.travel_builder.Models.OpenAiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("v1/chat/completions")
    fun getChatCompletions(@Body request: OpenAiRequest): Call<OpenAiResponse>
}
