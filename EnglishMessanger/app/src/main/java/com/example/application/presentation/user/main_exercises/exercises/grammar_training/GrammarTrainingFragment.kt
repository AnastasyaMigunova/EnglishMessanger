package com.example.application.presentation.user.main_exercises.exercises.grammar_training

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.application.R
import com.example.application.databinding.FragmentGrammarTrainingBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.onboarding.interests.InterestsViewModel
import com.example.application.server.ApiClientExercises
import kotlinx.coroutines.launch

class GrammarTrainingFragment : Fragment(R.layout.fragment_grammar_training) {

    private val binding by viewBinding(FragmentGrammarTrainingBinding::bind)
    private var listener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.buttonMoveToExercises.setOnClickListener {
            val topic = binding.editTextChooseTopic.text
            if (topic.isEmpty()) {
                Toast.makeText(requireContext(), "Выберите тему для упражнений", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.imageViewCat.visibility = View.GONE
                binding.textViewOr.visibility = View.GONE
                binding.buttonMoveToExercises.visibility = View.GONE
                binding.editTextChooseTopic.visibility = View.GONE
                binding.textViewGrammarDescription.visibility = View.GONE

                binding.editTextChooseTopic.text = null
                getTopicExercises(topic.toString())
            }
        }
    }

    private fun getTopicExercises(topic: String) {
        lifecycleScope.launch {

            Glide.with(requireContext())
                .asGif()
                .load(R.raw.racoon)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageViewGif)

            binding.imageViewGif.visibility = View.VISIBLE

            try {
                val exercises = ApiClientExercises.service.getSentencesExercises(topic)
                val fragment = TopicExercisesFragment.newInstance(ArrayList(exercises))
                listener?.onButtonCLick(fragment, "TOPIC_EXERCISES")
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }

            binding.imageViewGif.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): GrammarTrainingFragment {
            return GrammarTrainingFragment()
        }
    }
}