package com.example.application.presentation.user.main_exercises.cards.sets.set_info

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentSetInfoBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnItemClickListener
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class SetInfoFragment : Fragment(R.layout.fragment_set_info) {

    private val binding by viewBinding(FragmentSetInfoBinding::bind)
    private val itemsAdapter = ItemsAdapter()
    private var listener: OnItemClickListener? = null
    private var buttonListener: OnButtonClickListener? = null
    private lateinit var cardSet: CardSet
    private lateinit var set: CardSet

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnItemClickListener
        buttonListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCardSets.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments.let {
            cardSet = it?.getParcelable(CARD_SET)!!
        }

        binding.textViewSetTitle.text = cardSet.title

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        email?.let { getCardSet(it) }
    }

    private fun getCardSet(email: String) {
        lifecycleScope.launch {
            try {
                set = cardSet.id?.let { ApiClient.service.getCardSet(email, it) }!!
                binding.recyclerViewCardSets.adapter = itemsAdapter.apply {
                    listener = object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            buttonListener?.onButtonCLick(
                                SetWordsFragment.newInstance(
                                    cardSet
                                ),
                                "SET_WORDS"
                            )
                        }

                        override fun onItemExerciseClick(
                            rightAnswer: String,
                            currentAnswer: String
                        ) {
                        }

                        override fun onItemSetClick(cardSet: CardSet) {}
                    }
                }
                set.toLearn?.let { itemsAdapter.setList(it) }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        private const val CARD_SET = "card_set"

        fun newInstance(cardSet: CardSet): SetInfoFragment {
            return SetInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CARD_SET, cardSet)
                }
            }
        }
    }
}
