package com.example.application.presentation.user.main_exercises.cards.studying

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentCardsStudyingWordsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.cards.adding_word.AddWordFragment
import com.example.application.presentation.user.main_exercises.cards.sets.CardsSetsFragment
import com.example.application.presentation.user.main_exercises.cards.studying.ItemsAdapter
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class CardsStudyingWordsFragment : Fragment(R.layout.fragment_cards_studying_words) {

    private val binding by viewBinding(FragmentCardsStudyingWordsBinding::bind)
    private val itemsAdapter = ItemsAdapter()
    private var listener: OnButtonClickListener? = null
    private val addingWordFragment = AddWordFragment.newInstance()
    private val cardSetFragment = CardsSetsFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewStudyingWords.layoutManager = layoutManager

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        getStudyingWords()

        binding.buttonLearnedWords.setOnClickListener {
            listener?.onButtonCLick(cardSetFragment, "CARD_SET")
        }

        addingWordFragment.setOnDismissListener(object : AddWordFragment.OnDismissListener {
            override fun onDismiss() {
                getStudyingWords()
            }
        })

        binding.cardViewAddWord.setOnClickListener {
            val args = Bundle()
            args.putString("flag", "0")
            addingWordFragment.arguments = args
            addingWordFragment.show(parentFragmentManager, addingWordFragment.tag)
        }
    }

    private fun getStudyingWords() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        lifecycleScope.launch {
            try {
                val studyingWords = email?.let { ApiClient.service.getTotalToLearn(it) }
                if (studyingWords!!.isNotEmpty()) {
                    binding.recyclerViewStudyingWords.visibility = View.VISIBLE
                    binding.buttonLearnedWords.visibility = View.VISIBLE
                    binding.textViewDescriptionEmpty.visibility = View.GONE

                    binding.recyclerViewStudyingWords.adapter = itemsAdapter
                    itemsAdapter.setList(studyingWords)
                } else {
                    binding.recyclerViewStudyingWords.visibility = View.GONE
                    binding.buttonLearnedWords.visibility = View.GONE
                    binding.textViewDescriptionEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance() : CardsStudyingWordsFragment {
            return CardsStudyingWordsFragment()
        }
    }
}