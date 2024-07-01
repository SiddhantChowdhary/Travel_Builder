package com.example.travel_builder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.travel_builder.ModelView.ChatViewModel
import com.example.travel_builder.Services.ApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var editTextQuestion: EditText
    private lateinit var btnSubmit: Button
    private lateinit var textViewAnswer: TextView
    private lateinit var loader: ProgressBar
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeUI()

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            val question: String = editTextQuestion.text.toString()
            textViewAnswer.text=""
            // Trigger the ViewModel to make the chat request
            viewModel.makeChatRequest(question)
        }

        // Observe changes in answerLiveData
        viewModel.answerLiveData.observe(this) { answer ->
            textViewAnswer.text = answer
        }

        // Observe changes in loaderVisibilityLiveData
        viewModel.loaderVisibilityLiveData.observe(this) { visibility ->
            loader.visibility = visibility
        }
    }

    /**
     * Initialize UI components.
     */
    private fun initializeUI() {
        editTextQuestion = findViewById(R.id.question)
        btnSubmit = findViewById(R.id.enter)
        textViewAnswer = findViewById(R.id.answer)
        loader = findViewById(R.id.progressBar)
        loader.visibility = View.GONE
    }
}
