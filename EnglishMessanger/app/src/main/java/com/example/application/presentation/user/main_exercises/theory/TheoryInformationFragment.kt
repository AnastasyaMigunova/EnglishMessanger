package com.example.application.presentation.user.main_exercises.theory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Theory
import com.example.application.databinding.FragmentTheoryInformationBinding

class TheoryInformationFragment : Fragment(R.layout.fragment_theory_information) {

    private val binding by viewBinding(FragmentTheoryInformationBinding::bind)
    private lateinit var theory: Theory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments.let {
            theory = it?.getParcelable(THEORY)!!
        }

        binding.textViewNameTheory.text = theory.title
        binding.textViewExplanation.text = theory.explanation
        binding.textViewExample.text = theory.example
        binding.textViewMistakes.text = theory.commonMistakeDescription
        binding.textViewWrong.text = theory.cmWrong
        binding.textViewCorrect.text = theory.cmRight
    }

    companion object {
        private const val THEORY = "theory"

        fun newInstance(theory: Theory): TheoryInformationFragment =
            TheoryInformationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(THEORY, theory)
                }
            }
    }
}