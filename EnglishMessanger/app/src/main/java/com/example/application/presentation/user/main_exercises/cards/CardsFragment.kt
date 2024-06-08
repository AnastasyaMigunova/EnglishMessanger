package com.example.application.presentation.user.main_exercises.cards

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentCardsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.cards.learned.CardsLearnedWordsFragment
import com.example.application.presentation.user.main_exercises.cards.sets.CardsSetsFragment
import com.example.application.presentation.user.main_exercises.cards.studying.CardsStudyingWordsFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val binding by viewBinding(FragmentCardsBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val cardsLearnedFragment = CardsLearnedWordsFragment.newInstance()
    private val cardsStudyingFragment = CardsStudyingWordsFragment.newInstance()
    private val cardSetFragment = CardsSetsFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardItemLearnedWords.imageViewItem.setImageResource(R.mipmap.icon_checkmark_foreground)
        binding.cardItemLearnedWords.textViewItemName.text = getString(R.string.learned_words)
        binding.cardItemLearnedWords.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.background
            )
        )

        binding.cardItemStudyingWords.imageViewItem.setImageResource(R.mipmap.icon_brain_foreground)
        binding.cardItemStudyingWords.textViewItemName.text = getString(R.string.studying_words)
        binding.cardItemStudyingWords.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.border
            )
        )
        binding.cardItemStudyingWords.textViewItemName.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        getCountWords()

        binding.cardItemStudyingWords.textCountWord.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.cardItemLearnedWords.textCountWord.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.start_text
            )
        )

        binding.cardItemLearnedWords.cardView.setOnClickListener {
            listener?.onButtonCLick(cardsLearnedFragment, "CARDS_LEARNED")
        }

        binding.cardItemStudyingWords.cardView.setOnClickListener {
            listener?.onButtonCLick(cardsStudyingFragment, "CARDS_STUDYING")
        }

        binding.buttonLearnedWords.setOnClickListener {
            listener?.onButtonCLick(cardSetFragment, "CARD_SET")
        }

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun getCountWords() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        lifecycleScope.launch {
            try {
                val learnedWords = email?.let { ApiClient.service.getTotalLearned(it) }
                val studyingWords = email?.let { ApiClient.service.getTotalToLearn(it) }

                if (learnedWords!!.isNotEmpty()) {
                    binding.cardItemLearnedWords.textCountWord.text = learnedWords.size.toString()
                } else {
                    binding.cardItemLearnedWords.textCountWord.text = "0"
                }
                if (studyingWords!!.isNotEmpty()) {
                    binding.cardItemStudyingWords.textCountWord.text = studyingWords.size.toString()
                } else {
                    binding.cardItemStudyingWords.textCountWord.text = "0"
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): CardsFragment {
            return CardsFragment()
        }
    }
}