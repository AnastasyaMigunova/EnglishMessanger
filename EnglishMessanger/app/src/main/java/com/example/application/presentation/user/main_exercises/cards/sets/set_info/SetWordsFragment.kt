package com.example.application.presentation.user.main_exercises.cards.sets.set_info

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Card
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentSetWordsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.cards.sets.CardsSetsFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch
import kotlin.math.abs

class SetWordsFragment : Fragment(R.layout.fragment_set_words) {

    private val binding by viewBinding(FragmentSetWordsBinding::bind)
    private lateinit var cardSet: CardSet
    private var learnedWords: MutableList<Card> = mutableListOf()
    private var toLearnWords: MutableList<Card> = mutableListOf()
    private var isFrontVisible = true
    private var currentIndex = 0
    private var size: Int = 0
    private var count = 1
    private var buttonListener: OnButtonClickListener? = null
    private lateinit var gestureDetector: GestureDetector

    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = activity as? OnButtonClickListener
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imageViewResult.visibility = View.GONE
        binding.buttonResult.visibility = View.GONE
        binding.textViewResult.visibility = View.GONE

        arguments?.let {
            cardSet = it.getParcelable(CARD_WORDS)!!
            toLearnWords = cardSet.toLearn?.toMutableList()!!
        }

        size = toLearnWords.size
        binding.textViewToLearnWords.text = "1/$size"

        updateCardContent()

        gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                private val SWIPE_THRESHOLD = 100
                private val SWIPE_VELOCITY_THRESHOLD = 100

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null || e2 == null) return false
                    val diffX = e2.x - e1.x
                    val diffY = e2.y - e1.y
                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            return true
                        }
                    }
                    return false
                }

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    flipCard()
                    return true
                }
            })

        binding.cardViewWord.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun flipCard() {
        val scale = requireContext().resources.displayMetrics.density
        binding.cardViewWord.cameraDistance = 8000 * scale

        val flipOutAnimatorSet =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_out) as AnimatorSet
        val flipInAnimatorSet =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_in) as AnimatorSet

        flipOutAnimatorSet.setTarget(binding.cardViewWord)
        flipInAnimatorSet.setTarget(binding.cardViewWord)

        flipOutAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.textViewWord.text =
                    if (isFrontVisible) toLearnWords[currentIndex].explanation else toLearnWords[currentIndex].text
                isFrontVisible = !isFrontVisible
                flipInAnimatorSet.start()
            }
        })

        flipOutAnimatorSet.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCardContent() {
        binding.textViewWord.text = toLearnWords[currentIndex].text
        binding.textViewToLearnWords.text = "${count}/${size}"
        isFrontVisible = true
    }

    private fun onSwipeLeft() {
        if (currentIndex < toLearnWords.size - 1) {
            currentIndex++
            count++
            animateCardChange(true)
        } else {
            showCompletionView()
        }
    }

    private fun onSwipeRight() {
        if (toLearnWords.isNotEmpty()) {
            val removedItem = toLearnWords.removeAt(currentIndex)
            learnedWords.add(removedItem)
            saveToLearned(removedItem)
            count++

            if (toLearnWords.isEmpty()) {
                showCompletionView()
            } else {
                if (currentIndex >= toLearnWords.size) {
                    currentIndex = 0
                }
                animateCardChange(false)
            }
        }
    }

    private fun showCompletionView() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        binding.imageViewResult.visibility = View.VISIBLE
        binding.buttonResult.visibility = View.VISIBLE
        binding.textViewResult.visibility = View.VISIBLE

        binding.cardViewWord.visibility = View.GONE
        binding.textViewFlipCard.visibility = View.GONE

        if (learnedWords.size == size) {
            binding.imageViewResult.setImageResource(R.mipmap.icon_party_foreground)
            binding.buttonResult.text = getString(R.string.start_again)
            binding.textViewResult.text = getString(R.string.all_cards_learned)

            binding.buttonResult.setOnClickListener {
                email?.let { email -> refreshSet(cardSet, email) }
                requireActivity().supportFragmentManager.popBackStack()
            }
        } else {
            binding.imageViewResult.setImageResource(R.mipmap.icon_cat_foreground)
            binding.buttonResult.text = getString(R.string.keep_learning)
            binding.textViewResult.text = getString(R.string.cards_keep_learning)

            binding.buttonResult.setOnClickListener {
                buttonListener?.onButtonCLick(CardsSetsFragment.newInstance(), "SET_INFO")
            }
        }
    }

    private fun saveToLearned(card: Card) {
        lifecycleScope.launch {
            try {
                val answer = ApiClient.service.saveToLearned(card)
                Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun refreshSet(cardSet: CardSet, email: String) {
        lifecycleScope.launch {
            try {
                val answer = cardSet.id?.let { ApiClient.service.refreshCards(it, email) }
                Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun animateCardChange(isSwipeLeft: Boolean) {
        val targetX =
            if (isSwipeLeft) -binding.cardViewWord.width.toFloat() else binding.cardViewWord.width.toFloat()

        binding.cardViewWord.animate()
            .translationX(targetX)
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    updateCardContent()

                    binding.cardViewWord.translationX = -targetX
                    binding.cardViewWord.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(300)
                        .setListener(null)
                        .start()
                }
            })
            .start()
    }

    companion object {
        private const val CARD_WORDS = "card_words"

        fun newInstance(cardSet: CardSet): SetWordsFragment {
            return SetWordsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CARD_WORDS, cardSet)
                }
            }
        }
    }
}