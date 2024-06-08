package com.example.application.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentStartBinding
import com.example.application.`interface`.OnButtonClickListener

class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding by viewBinding(FragmentStartBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val fragmentAuth = AuthFragment.newInstance()
    private val fragmentRegister = RegisterFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar

        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setDisplayShowTitleEnabled(false)
        }

        binding.buttonAuth.setOnClickListener {
            listener?.onButtonCLick(fragmentAuth, "AUTH")
        }

        binding.buttonRegister.setOnClickListener {
            listener?.onButtonCLick(fragmentRegister, "REGISTER")
        }
    }

    companion object {
        fun newInstance(): StartFragment {
            return StartFragment()
        }
    }
}