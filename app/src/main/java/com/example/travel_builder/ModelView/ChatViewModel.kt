package com.example.travel_builder.ModelView

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travel_builder.Services.ApiClient

class ChatViewModel : ViewModel() {
    val answerLiveData = MutableLiveData<String>()
    val loaderVisibilityLiveData = MutableLiveData<Int>()

    fun makeChatRequest(que: String) {
        loaderVisibilityLiveData.value = View.VISIBLE
        ApiClient.makeChatRequest(que, answerLiveData, loaderVisibilityLiveData)
    }
}