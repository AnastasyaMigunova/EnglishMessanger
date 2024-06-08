package com.example.application.presentation.user.main_exercises

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentMainExercisesBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.cards.CardsFragment
import com.example.application.presentation.user.main_exercises.exercises.ExercisesFragment
import com.example.application.presentation.user.main_exercises.theory.TheoryFragment

class MainExercisesFragment : Fragment(R.layout.fragment_main_exercises) {

    private val binding by viewBinding(FragmentMainExercisesBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val cardsFragment = CardsFragment.newInstance()
    private val exercisesFragment = ExercisesFragment.newInstance()
    private val theoryFragment = TheoryFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemCards.textViewItemName.text = getString(R.string.cards)
        binding.itemCards.textViewItemDescription.text = getString(R.string.cards_description)

        binding.itemTheory.textViewItemName.text = getString(R.string.theory)
        binding.itemTheory.textViewItemDescription.text = getString(R.string.theory_description)

        binding.itemExercises.textViewItemName.text = getString(R.string.exercises)
        binding.itemExercises.textViewItemDescription.text = getString(R.string.exercises_description)

        binding.itemCards.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(cardsFragment, "CARDS")
        }

        binding.itemTheory.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(theoryFragment, "THEORY")
        }

        binding.itemExercises.cardViewItemExercises.setOnClickListener {
            listener?.onButtonCLick(exercisesFragment, "EXERCISES")
        }
    }

    override fun onResume() {
        super.onResume()

        binding.itemCards.textViewItemName.text = getString(R.string.cards)
        binding.itemCards.textViewItemDescription.text = getString(R.string.cards_description)

        binding.itemTheory.textViewItemName.text = getString(R.string.theory)
        binding.itemTheory.textViewItemDescription.text = getString(R.string.theory_description)

        binding.itemExercises.textViewItemName.text = getString(R.string.exercises)
        binding.itemExercises.textViewItemDescription.text = getString(R.string.exercises_description)
    }

    companion object {
        fun newInstance(): MainExercisesFragment {
            return MainExercisesFragment()
        }
    }
}