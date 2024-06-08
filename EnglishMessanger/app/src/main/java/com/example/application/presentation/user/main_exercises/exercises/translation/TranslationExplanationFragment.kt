package com.example.application.presentation.user.main_exercises.exercises.translation

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.TranslationModel
import com.example.application.databinding.FragmentQaExplanationBinding
import com.example.application.server.ApiClient
import com.example.application.server.ApiClientExercises
import kotlinx.coroutines.launch

class TranslationExplanationFragment : Fragment(R.layout.fragment_qa_explanation) {

    private val binding by viewBinding(FragmentQaExplanationBinding::bind)
    private lateinit var explanations: TranslationModel
    private lateinit var originalText: String
    private lateinit var text: String
    private lateinit var level: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments.let {
            originalText = it?.getString(ORIGINAL_TEXT, "").toString()
            text = it?.getString(TEXT, "").toString()
            level = it?.getString(LEVEL, "").toString()
        }

        binding.textViewQuestion.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewQuestion.textViewItemName.text = getString(R.string.text)
        binding.textViewQuestion.textViewItemDescription.text = originalText

        binding.textViewYourAnswer.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewYourAnswer.textViewItemName.text = getString(R.string.translation)
        binding.textViewYourAnswer.textViewItemDescription.text = text

        binding.textViewCorrectAnswer.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewCorrectAnswer.textViewItemName.text =
            getString(R.string.correct_translation)

        binding.textViewExplanation.textViewItemName.setTypeface(null, Typeface.BOLD)
        binding.textViewExplanation.textViewItemName.text = getString(R.string.explanation)

        sendTranslationExercise(originalText, text, level)
    }

    private fun sendTranslationExercise(originalText: String, text: String, level: String) {
        lifecycleScope.launch {
            try {
                explanations =
                    ApiClientExercises.service.sendTranslationExercise(originalText, text, level)


                binding.textViewCorrectAnswer.textViewItemDescription.text =
                    explanations.corrected_text

                binding.textViewExplanation.textViewItemDescription.text =
                    explanations.explanations.joinToString("\n")

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        private const val ORIGINAL_TEXT = "original_text"
        private const val TEXT = "text"
        private const val LEVEL = "level"

        fun newInstance(
            originalText: String,
            text: String,
            level: String
        ): TranslationExplanationFragment =
            TranslationExplanationFragment().apply {
                arguments = Bundle().apply {
                    putString(ORIGINAL_TEXT, originalText)
                    putString(TEXT, text)
                    putString(LEVEL, level)
                }
            }
    }
}