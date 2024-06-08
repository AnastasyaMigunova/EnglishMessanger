package com.example.application.presentation.user.main_exercises.cards.learned

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentCardsLearnedWordsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.cards.adding_word.AddWordFragment
import com.example.application.server.ApiClient
import com.example.application.presentation.user.main_exercises.cards.learned.ItemsAdapter
import com.example.application.presentation.user.main_exercises.cards.sets.CardsSetsFragment
import kotlinx.coroutines.launch

class CardsLearnedWordsFragment : Fragment(R.layout.fragment_cards_learned_words) {

    private val binding by viewBinding(FragmentCardsLearnedWordsBinding::bind)
    private val itemsAdapter = ItemsAdapter()
    private var listener: OnButtonClickListener? = null
    private val cardSetFragment = CardsSetsFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewLearnedWords.layoutManager = layoutManager

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.buttonLearnedWords.setOnClickListener {
            listener?.onButtonCLick(cardSetFragment, "CARD_SET")

        }

        getLearnedCountWords()
    }

    private fun getLearnedCountWords() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        lifecycleScope.launch {
            try {
                val learnedWords = email?.let { ApiClient.service.getTotalLearned(it) }
                if (learnedWords!!.isNotEmpty()) {
                    binding.recyclerViewLearnedWords.visibility = View.VISIBLE
                    binding.buttonLearnedWords.visibility = View.VISIBLE
                    binding.imageViewItem.visibility = View.GONE
                    binding.textViewTitleEmpty.visibility = View.GONE
                    binding.textViewDescriptionEmpty.visibility = View.GONE

                    binding.recyclerViewLearnedWords.adapter = itemsAdapter
                    itemsAdapter.setList(learnedWords)
                } else {
                    binding.recyclerViewLearnedWords.visibility = View.GONE
                    binding.buttonLearnedWords.visibility = View.GONE
                    binding.imageViewItem.visibility = View.VISIBLE
                    binding.textViewTitleEmpty.visibility = View.VISIBLE
                    binding.textViewDescriptionEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): CardsLearnedWordsFragment {
            return CardsLearnedWordsFragment()
        }
    }
}