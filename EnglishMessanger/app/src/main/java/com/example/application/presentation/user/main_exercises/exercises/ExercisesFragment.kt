package com.example.application.presentation.user.main_exercises.exercises

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentExercisesBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.exercises.grammar_training.GrammarTrainingFragment
import com.example.application.presentation.user.main_exercises.exercises.qa.QaFragment
import com.example.application.presentation.user.main_exercises.exercises.translation.TranslationFragment

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private val binding by viewBinding(FragmentExercisesBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val grammarTrainingFragment = GrammarTrainingFragment.newInstance()
    private val qaFragment = QaFragment.newInstance()
    private val translationFragment = TranslationFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.itemGrammarTraining.textViewItemName.text = getString(R.string.grammar_training_title)
        binding.itemGrammarTraining.textViewItemDescription.text = getString(R.string.grammar_training_description)

        binding.itemQA.textViewItemName.text = getString(R.string.qa_title)
        binding.itemQA.textViewItemDescription.text = getString(R.string.qa_description)

        binding.itemTranslation.textViewItemName.text = getString(R.string.translation_title)
        binding.itemTranslation.textViewItemDescription.text = getString(R.string.translation_description)

        binding.itemGrammarTraining.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(grammarTrainingFragment, "GRAMMAR_TRAINING")
        }

        binding.itemQA.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(qaFragment, "QA")
        }

        binding.itemTranslation.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(translationFragment, "TRANSLATION")
        }
    }

    companion object {
        fun newInstance() : ExercisesFragment {
            return ExercisesFragment()
        }
    }
}