package com.example.application.presentation.user.main_exercises.cards.sets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentCardsSetsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnItemClickListener
import com.example.application.presentation.user.main_exercises.cards.adding_word.AddWordFragment
import com.example.application.presentation.user.main_exercises.cards.sets.set_info.SetInfoFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class CardsSetsFragment : Fragment(R.layout.fragment_cards_sets) {

    private val binding by viewBinding(FragmentCardsSetsBinding::bind)
    private val itemsAdapter = ItemsAdapter()
    private var listener: OnItemClickListener? = null
    private var buttonListener: OnButtonClickListener? = null
    private val addingWordFragment = AddWordFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnItemClickListener
        buttonListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewCardSets.layoutManager = layoutManager

        getAllCardSets()

        binding.cardViewAddSet.setOnClickListener {
            val args = Bundle()
            args.putString("flag", "1")
            addingWordFragment.arguments = args
            addingWordFragment.show(parentFragmentManager, addingWordFragment.tag)
            getAllCardSets()
        }

        addingWordFragment.setOnDismissListener(object : AddWordFragment.OnDismissListener {
            override fun onDismiss() {
                getAllCardSets()
            }
        })

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllCardSets() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        lifecycleScope.launch {
            try {
                val cardSets = email?.let { ApiClient.service.getAllCardSets(it) }
                if (cardSets!!.isNotEmpty()) {
                    binding.recyclerViewCardSets.adapter = itemsAdapter.apply {
                        listener = object : OnItemClickListener {
                            override fun onItemClick(position: Int) {}
                            override fun onItemExerciseClick(
                                rightAnswer: String,
                                currentAnswer: String
                            ) {}
                            override fun onItemSetClick(cardSet: CardSet) {
                                buttonListener?.onButtonCLick(
                                    SetInfoFragment.newInstance(cardSet),
                                    "SET_INFO"
                                )
                            }
                        }
                    }
                    itemsAdapter.setList(cardSets)
                    itemsAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): CardsSetsFragment {
            return CardsSetsFragment()
        }
    }
}