package com.example.application.presentation.user.main_exercises.exercises.translation

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
import com.example.application.databinding.FragmentTranslationBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.server.ApiClient
import com.example.application.server.ApiClientExercises
import kotlinx.coroutines.launch

class TranslationFragment : Fragment(R.layout.fragment_translation) {

    private val binding by viewBinding(FragmentTranslationBinding::bind)
    private var interestingTopic: String = ""
    private var listener: OnButtonClickListener? = null
    private var flag: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewInterestingTopic.visibility = View.GONE

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val level = sharedPreferences.getString("level", "")

        binding.buttonGenerateText.setOnClickListener {
            if (flag) {
                val text = binding.editTranslationTopic.text.toString()
                binding.editTranslationTopic.text = null
                listener?.onButtonCLick(
                    TranslationExplanationFragment.newInstance(
                        interestingTopic,
                        text,
                        level!!
                    ),
                    "TRANSLATION_EXPLANATION"
                )
            } else {
                getTranslationExercise(binding.editTranslationTopic.text.toString(), level!!)
                flag = true
            }

        }
    }

    private fun getTranslationExercise(topic: String, level: String) {
        lifecycleScope.launch {

            binding.buttonGenerateText.visibility = View.GONE
            binding.editTranslationTopic.visibility = View.GONE

            Glide.with(requireContext())
                .asGif()
                .load(R.raw.racoon)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageViewGif)

            binding.imageViewGif.visibility = View.VISIBLE

            try {
                interestingTopic = ApiClientExercises.service.getTranslationExercise(topic, level)

                binding.buttonGenerateText.visibility = View.VISIBLE
                binding.buttonGenerateText.text = getString(R.string.check)

                binding.editTranslationTopic.visibility = View.VISIBLE
                binding.editTranslationTopic.text = null
                binding.editTranslationTopic.hint = getString(R.string.translate_text)

                binding.textViewInterestingTopic.visibility = View.VISIBLE
                binding.textViewInterestingTopic.text = interestingTopic

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }

            binding.imageViewGif.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): TranslationFragment {
            return TranslationFragment()
        }
    }
}