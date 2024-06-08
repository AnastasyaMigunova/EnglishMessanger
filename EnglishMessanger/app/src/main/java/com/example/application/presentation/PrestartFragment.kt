package com.example.application.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentPrestartBinding
import com.example.application.`interface`.OnButtonClickListener

class PrestartFragment : Fragment(R.layout.fragment_prestart) {

    private val binding by viewBinding(FragmentPrestartBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val fragment = StartFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener {
            listener?.onButtonCLick(fragment, "START")
        }
    }

    companion object {
        fun newInstance() : PrestartFragment {
            return PrestartFragment()
        }
    }
}