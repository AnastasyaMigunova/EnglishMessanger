package com.example.application.presentation.user.main_exercises.exercises.qa

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.application.R
import com.example.application.databinding.FragmentQaBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.main_exercises.exercises.grammar_training.TopicExercisesFragment
import com.example.application.server.ApiClient
import com.example.application.server.ApiClientExercises
import kotlinx.coroutines.launch
import java.util.ArrayList

class QaFragment : Fragment(R.layout.fragment_qa) {

    private val binding by viewBinding(FragmentQaBinding::bind)
    private var listener: OnButtonClickListener? = null
    private var question: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val level = sharedPreferences.getString("level", "")

        binding.buttonGenerateQuestion.visibility = View.VISIBLE
        binding.editTextAnswer.visibility = View.GONE
        binding.buttonCheckAnswer.visibility = View.GONE

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.buttonGenerateQuestion.setOnClickListener {
            level?.let { level -> getQuestion(level) }
        }

        binding.buttonCheckAnswer.setOnClickListener {
            listener?.onButtonCLick(
                QaExplanationFragment.newInstance(
                    question,
                    binding.editTextAnswer.text.toString()
                ),
                "QA_EXPLANATION"
            )
        }
    }

    private fun getQuestion(level: String) {
        lifecycleScope.launch {

            binding.buttonGenerateQuestion.visibility = View.GONE

            Glide.with(requireContext())
                .asGif()
                .load(R.raw.racoon)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageViewGif)

            binding.imageViewGif.visibility = View.VISIBLE

            try {
                question = ApiClientExercises.service.getQuestion(level)

                binding.editTextAnswer.visibility = View.VISIBLE
                binding.buttonCheckAnswer.visibility = View.VISIBLE

                binding.textViewQuestion.text = question

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }

            binding.imageViewGif.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): QaFragment {
            return QaFragment()
        }
    }
}