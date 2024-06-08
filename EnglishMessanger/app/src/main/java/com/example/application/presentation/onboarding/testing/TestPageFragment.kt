package com.example.application.presentation.onboarding.testing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Answer
import com.example.application.data.model.Question
import com.example.application.databinding.FragmentTestPageBinding
import com.example.application.databinding.ItemAnswerBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnNextPageListener

class TestPageFragment : Fragment(R.layout.fragment_test_page) {

    private val binding by viewBinding(FragmentTestPageBinding::bind)
    private var question: Question? = null
    private var position: Int? = null
    private var size: Int? = null
    private var listener: OnNextPageListener? = null
    private var fragmentListener: OnButtonClickListener? = null
    private val fragment = ResultTestFragment.newInstance()
    private lateinit var viewModel: AnswersViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireParentFragment() as? OnNextPageListener
        fragmentListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AnswersViewModel::class.java]

        arguments?.let {
            question = it.getParcelable(ARG_QUESTION)
            position = it.getInt(ARG_POSITION)
            size = it.getInt(ARG_PAGERSIZE)
        }

        val firstQuestion = binding.firstQuestion
        val secondQuestion = binding.secondQuestion
        val thirdQuestion = binding.thirdQuestion
        val fourthQuestion = binding.fourthQuestion

        binding.textView.text = question?.question
        firstQuestion.textViewInterest.text = question?.answerOne
        secondQuestion.textViewInterest.text = question?.answerTwo
        thirdQuestion.textViewInterest.text = question?.answerThree

        if (question?.answerFour == null) {
            fourthQuestion.cardViewInterest.visibility = View.GONE
        } else {
            fourthQuestion.cardViewInterest.visibility = View.VISIBLE
            fourthQuestion.textViewInterest.visibility = View.VISIBLE
            fourthQuestion.textViewInterest.text = question?.answerFour
            fourthQuestion.cardViewInterest.setOnClickListener {
                selectedColorAnswer(fourthQuestion)
                val answer = Answer(
                    questionId = question?.id!!.toInt(),
                    currentAnswer = question?.answerFour.toString()
                )
                viewModel.addItem(answer)
            }
        }

        firstQuestion.cardViewInterest.setOnClickListener {
            selectedColorAnswer(firstQuestion)
            val answer = Answer(
                questionId = question?.id!!.toInt(),
                currentAnswer = question?.answerOne.toString()
            )
            viewModel.addItem(answer)
        }

        secondQuestion.cardViewInterest.setOnClickListener {
            selectedColorAnswer(secondQuestion)
            val answer = Answer(
                questionId = question?.id!!.toInt(),
                currentAnswer = question?.answerTwo.toString()
            )
            viewModel.addItem(answer)
        }

        thirdQuestion.cardViewInterest.setOnClickListener {
            selectedColorAnswer(thirdQuestion)
            val answer = Answer(
                questionId = question?.id!!.toInt(),
                currentAnswer = question?.answerThree.toString()
            )
            viewModel.addItem(answer)
        }

        if (position!! + 1 == size) {
            binding.buttonTestResults.visibility = View.VISIBLE
        }
    }

    private fun selectedColorAnswer(answer: ItemAnswerBinding) {
        answer.cardViewInterest.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.start_text
            )
        )
        answer.textViewInterest.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.buttonTestResults.setOnClickListener {
            if (position!! + 1 == size) {
                fragmentListener?.onButtonCLick(fragment, "RESULT_TEST")
            }
        }
        listener?.onNextPage(position!! + 1)
    }

    companion object {
        private const val ARG_QUESTION = "question"
        private const val ARG_POSITION = "position"
        private const val ARG_PAGERSIZE = "pager_size"

        fun newInstance(question: Question, position: Int, size: Int) =
            TestPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION, question)
                    putInt(ARG_POSITION, position)
                    putInt(ARG_PAGERSIZE, size)
                }
            }
    }
}
