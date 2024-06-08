package com.example.application.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentDateBirthBinding

class DateBirthFragment : Fragment(R.layout.fragment_date_birth) {

    private val binding by viewBinding(FragmentDateBirthBinding::bind)
    private val handler = Handler(Looper.getMainLooper())

    private val saveRunnable = Runnable {
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("dateOfBirth", binding.editDateOfBirth.text.toString())
        editor.apply()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            handler.removeCallbacks(saveRunnable)
            handler.postDelayed(saveRunnable, 500)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editDateOfBirth = binding.editDateOfBirth
        editDateOfBirth.addTextChangedListener(DateInputMask(editDateOfBirth))
        editDateOfBirth.addTextChangedListener(textWatcher)
    }

    companion object {
        fun newInstance(): DateBirthFragment {
            return DateBirthFragment()
        }
    }
}