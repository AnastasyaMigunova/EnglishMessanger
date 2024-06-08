package com.example.application.presentation.user.main_exercises.exercises.grammar_training

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.SentenceExercise
import com.example.application.databinding.ItemSentenceExerciseBinding
import com.example.application.`interface`.OnItemClickListener
import com.example.application.presentation.onboarding.interests.InterestsViewModel

class GrammarTrainingViewHolder(
    parent: ViewGroup,
    private val context: Context
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_sentence_exercise, parent, false),
) {
    private var currentSentenceExercise: SentenceExercise? = null

    private val binding by viewBinding(ItemSentenceExerciseBinding::bind)
    val editText = binding.editTextAnswer
    val textView = binding.textViewRightAnswer

    private val handler = Handler(Looper.getMainLooper())

    private val saveRunnable = Runnable {
        val inputText = binding.editTextAnswer.text?.toString() ?: ""
        currentSentenceExercise?.current_answer = inputText
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            handler.removeCallbacks(saveRunnable)
            handler.postDelayed(saveRunnable, 500)
        }
    }

    fun bind(sentenceExercise: SentenceExercise, position: Int) = with(binding) {
        currentSentenceExercise = sentenceExercise
        binding.textViewSentence.text = sentenceExercise.exercise
        editTextAnswer.addTextChangedListener(textWatcher)
    }
}