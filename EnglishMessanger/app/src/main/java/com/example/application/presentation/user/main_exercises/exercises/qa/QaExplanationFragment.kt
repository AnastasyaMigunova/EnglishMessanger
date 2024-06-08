package com.example.application.presentation.user.main_exercises.exercises.qa

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.QuestionExercise
import com.example.application.databinding.FragmentQaExplanationBinding
import com.example.application.server.ApiClient
import com.example.application.server.ApiClientExercises
import kotlinx.coroutines.launch

class QaExplanationFragment : Fragment(R.layout.fragment_qa_explanation) {

    private val binding by viewBinding(FragmentQaExplanationBinding::bind)
    private lateinit var question: String
    private lateinit var answer: String
    private lateinit var questionExercise: QuestionExercise

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments.let {
            question = it?.getString(QUESTION, "").toString()
            answer = it?.getString(ANSWER, "").toString()
        }

        binding.textViewQuestion.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewQuestion.textViewItemName.text = getString(R.string.question)
        binding.textViewQuestion.textViewItemDescription.text = question

        binding.textViewYourAnswer.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewYourAnswer.textViewItemName.text = getString(R.string.qa_your_answer)
        binding.textViewYourAnswer.textViewItemDescription.text = answer

        binding.textViewCorrectAnswer.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewCorrectAnswer.textViewItemName.text = getString(R.string.correct_answer)

        binding.textViewExplanation.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewExplanation.textViewItemName.text = getString(R.string.explanation)

        getExplanation(question, answer)

    }

    private fun getExplanation(question: String, answer: String) {
        lifecycleScope.launch {
            try {
                questionExercise = ApiClientExercises.service.getAnswer(question, answer)

                binding.textViewCorrectAnswer.textViewItemDescription.text =
                    questionExercise.corrected_answer

                binding.textViewExplanation.textViewItemDescription.text =
                    questionExercise.explanation.joinToString("\n")

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        private const val QUESTION = "question"
        private const val ANSWER = "answer"

        fun newInstance(question: String, answer: String): QaExplanationFragment =
            QaExplanationFragment().apply {
                arguments = Bundle().apply {
                    putString(QUESTION, question)
                    putString(ANSWER, answer)
                }
            }
    }
}