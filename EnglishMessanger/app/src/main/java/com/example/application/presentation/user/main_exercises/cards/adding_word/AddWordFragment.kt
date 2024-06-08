package com.example.application.presentation.user.main_exercises.cards.adding_word

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentAddWordBinding
import com.example.application.`interface`.OnItemClickListener
import com.example.application.server.ApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class AddWordFragment : BottomSheetDialogFragment(R.layout.fragment_add_word) {

    private val binding by viewBinding(FragmentAddWordBinding::bind)
    private var listener: OnItemClickListener? = null
    private val itemsAdapter = ItemsAdapter()
    private var selectedCardSet: CardSet? = null

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnItemClickListener
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    private var dismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val behavior = (dialog as? BottomSheetDialog)?.behavior
        if (behavior != null) {
            behavior.peekHeight = resources.displayMetrics.heightPixels / 2
            behavior.isDraggable = true
            behavior.skipCollapsed = true
        }

        binding.recyclerViewCardSets.visibility = View.VISIBLE

        val args = arguments
        if (args != null) {
            val value = args.getString("flag")
            if (value == "1") {
                binding.recyclerViewCardSets.visibility = View.GONE
                binding.editTextWordTitle.hint = getString(R.string.set_name)
                binding.editTextWordTranslation.hint = getString(R.string.set_description)

                val sharedPreferences =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val email = sharedPreferences.getString("email", "")

                binding.buttonAddWord.setOnClickListener {

                    val set = CardSet(
                        title = binding.editTextWordTitle.text.toString(),
                        description = binding.editTextWordTranslation.text.toString(),
                        userEmail = email.toString()
                    )
                    binding.editTextWordTitle.text = null
                    binding.editTextWordTranslation.text = null
                    createCardSet(set)
                }
            } else if (value == "2") {
                binding.recyclerViewCardSets.visibility = View.VISIBLE

                getAllCardSets()
                val sharedPreferences =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val searchWord = sharedPreferences.getString("word", "")

                lifecycleScope.launch {
                    try {
                        val word = ApiClient.service.findEngWord(searchWord!!)
                        binding.editTextWordTitle.setText(word.word)
                        binding.editTextWordTranslation.setText(word.description)
                    } catch (e: Exception) {
                        Log.e("ApiService", "Error fetching data: ${e.message}")
                    }
                }
                getAllCardSets()
                editor.remove("word")
                editor.apply()
            } else {
                binding.recyclerViewCardSets.visibility = View.VISIBLE
                binding.editTextWordTitle.hint = getString(R.string.word)
                binding.editTextWordTranslation.hint = getString(R.string.translation)

                getAllCardSets()
                binding.buttonAddWord.setOnClickListener {
                    val sharedPreferences =
                        requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                    val email = sharedPreferences.getString("email", "")

                    val card = email?.let { email ->
                        selectedCardSet?.let { set ->
                            set.id?.let { id ->
                                Card(
                                    setId = id,
                                    text = binding.editTextWordTitle.text.toString(),
                                    explanation = binding.editTextWordTranslation.text.toString(),
                                    userEmail = email
                                )
                            }
                        }
                    }
                    if (card != null) {
                        createCard(card)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Выберите сет карточек",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    binding.editTextWordTitle.text = null
                    binding.editTextWordTranslation.text = null
                }
            }
        }
    }

    private fun createCard(card: Card) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.service.createCard(card)
                Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun createCardSet(set: CardSet) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.service.createCardSet(set)
                Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT)
                    .show()
                getAllCardSets()
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun getAllCardSets() {
        lifecycleScope.launch {
            try {
                binding.recyclerViewCardSets.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

                val sharedPreferences =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val email = sharedPreferences.getString("email", "")

                val cardSets = email?.let { ApiClient.service.getAllCardSets(it) }

                if (cardSets != null) {
                    binding.recyclerViewCardSets.adapter = itemsAdapter.apply {
                        listener = object : OnItemClickListener {
                            override fun onItemClick(position: Int) {}
                            override fun onItemExerciseClick(
                                rightAnswer: String,
                                currentAnswer: String
                            ) {
                            }
                            override fun onItemSetClick(cardSet: CardSet) {
                                selectedCardSet = cardSet
                            }
                        }
                    }
                    itemsAdapter.setList(cardSets)
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): AddWordFragment {
            return AddWordFragment()
        }
    }
}