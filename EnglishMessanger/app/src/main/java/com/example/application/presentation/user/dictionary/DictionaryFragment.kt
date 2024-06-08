package com.example.application.presentation.user.dictionary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Word
import com.example.application.databinding.FragmentDictionaryBinding
import com.example.application.`interface`.OnDictionaryWordListener
import com.example.application.presentation.user.main_exercises.cards.adding_word.AddWordFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {

    private val binding by viewBinding(FragmentDictionaryBinding::bind)
    private val addWordFragment = AddWordFragment.newInstance()
    private lateinit var dictionaryViewModel: DictionaryViewModel
    private val itemsAdapter = ItemsAdapter()
    private lateinit var word: Word
    private var flag: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewChangeLanguage.visibility = View.GONE

        binding.recyclerViewDictionaryWords.layoutManager =
            LinearLayoutManager(requireContext())

        binding.editTextSearch.setOnLongClickListener {
            binding.cardViewChangeLanguage.visibility = View.VISIBLE
            true
        }

        binding.cardViewEngRus.setOnClickListener {
            binding.cardViewChangeLanguage.visibility = View.GONE
        }

        binding.cardViewRusEng.setOnClickListener {
            binding.cardViewChangeLanguage.visibility = View.GONE
            flag = false
        }

        dictionaryViewModel =
            ViewModelProvider(requireActivity())[DictionaryViewModel::class.java]

        binding.recyclerViewDictionaryWords.adapter = itemsAdapter.apply {
            listener = wordListener
        }

        binding.editTextSearch.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                findWord(binding.editTextSearch.text.toString())
                binding.editTextSearch.text = null
                return@setOnKeyListener true
            }
            false
        }
    }

    private val wordListener = object : OnDictionaryWordListener {
        override fun onItemWordClick(word: Word) {
            val sharedPreferences =
                requireContext().getSharedPreferences(
                    "UserData",
                    Context.MODE_PRIVATE
                )
            val editor = sharedPreferences.edit()
            editor.putString("word", word.word)
            editor.apply()

            val args = Bundle()
            args.putString("flag", "2")
            addWordFragment.arguments = args
            addWordFragment.show(parentFragmentManager, addWordFragment.tag)
        }
    }

    private fun findWord(searchWord: String) {
        lifecycleScope.launch {
            try {
                word = if (flag) {
                    ApiClient.service.findEngWord(searchWord)
                } else {
                    ApiClient.service.findRusWord(searchWord)[0]
                }
                dictionaryViewModel.addItem(word)

                val wordList: List<Word>? = dictionaryViewModel.getData().value
                wordList?.let {
                    val reversedList = it.toMutableList().reversed()
                    itemsAdapter.setList(reversedList)
                }

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): DictionaryFragment {
            return DictionaryFragment()
        }
    }
}