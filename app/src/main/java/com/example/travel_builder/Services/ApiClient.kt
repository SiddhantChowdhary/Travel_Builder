package com.example.travel_builder.Services

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.travel_builder.Models.Message
import com.example.travel_builder.Models.OpenAiRequest
import com.example.travel_builder.Models.OpenAiResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.openai.com/"

    // Lazy initialization of OpenAiApiService
    private val apiService: OpenAiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(3000, TimeUnit.SECONDS)
                    .readTimeout(3000, TimeUnit.SECONDS)
                    .writeTimeout(3000, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(OpenAiApiService::class.java)
    }

    /**
     * Make a chat request to the OpenAI API.
     *
     * @param que The user's question.
     * @param answerLiveData The LiveData to hold the API response.
     * @param loaderVisibilityLiveData The LiveData to manage the visibility of the loader.
     */
    fun makeChatRequest(
        que: String,
        answerLiveData: MutableLiveData<String>,
        loaderVisibilityLiveData: MutableLiveData<Int>
    ) {
        // Create the OpenAI request with the user's question
        val request = OpenAiRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(role = "system", content = "You are a helpful assistant."),
                Message(
                    role = "user",
                    content = "$que.Suggest me top 3 vacation destinations based on my information. Follow the below structure mentioned" +
                            "Brief overview message from you (humanly)" +
                            " List of the 3 places, each containing:" +
                            " 1. Name  2. Tourist attraction 3. Estimate flight tickets cost 4. Why it's relevant to you"
                )
            )
        )

        // Make the API call
        val call = apiService.getChatCompletions(request)
        call.enqueue(object : Callback<OpenAiResponse> {
            override fun onResponse(call: Call<OpenAiResponse>, response: Response<OpenAiResponse>) {
                // Handle API response
                handleApiResponse(response, answerLiveData, loaderVisibilityLiveData)
            }

            override fun onFailure(call: Call<OpenAiResponse>, t: Throwable) {
                // Handle API failure
                handleApiFailure(t, answerLiveData, loaderVisibilityLiveData)
            }
        })
    }

    /**
     * Handle the OpenAI API response.
     */
    private fun handleApiResponse(
        response: Response<OpenAiResponse>,
        answerLiveData: MutableLiveData<String>,
        loaderVisibilityLiveData: MutableLiveData<Int>
    ) {
        if (response.isSuccessful) {
            val rawJson = response.raw().toString()
            Log.d("ApiClient", "Raw JSON Response: $rawJson")

            // Extract and handle the API choices
            val openAiResponse = response.body()
            openAiResponse?.choices?.let {
                for (choice in it) {
                    val answer = choice.message
                    Log.d("ApiClient", "Answer: $answer")
                    answerLiveData.value = answer.content
                    loaderVisibilityLiveData.value = View.GONE
                }
            }
        } else {
            // Handle unsuccessful response
            loaderVisibilityLiveData.value = View.GONE
            Log.e("ApiClient", "Failure: ${response.code()}")
        }
    }

    /**
     * Handle OpenAI API failure.
     */
    private fun handleApiFailure(
        t: Throwable,
        answerLiveData: MutableLiveData<String>,
        loaderVisibilityLiveData: MutableLiveData<Int>
    ) {
        answerLiveData.value = ""
        loaderVisibilityLiveData.value = View.GONE
        Log.e("ApiClient", "Failure: ${t.message}", t)
    }
}
