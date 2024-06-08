package com.example.application.presentation

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.User
import com.example.application.databinding.FragmentRegisterBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.onboarding.OnboardingFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val fragment = OnboardingFragment.newInstance()
    private var flag: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewEye.setOnClickListener {
            if (flag) {
                binding.imageViewEye.setImageResource(R.mipmap.icon_eye_close_foreground)
                binding.editTextPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                flag = false
            } else {
                binding.imageViewEye.setImageResource(R.mipmap.icon_eye_foreground)
                binding.editTextPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                flag = true
            }
        }

        binding.buttonRegister.setOnClickListener {
            val inputFirstName = binding.editTextFirstName.text.toString()
            val inputLastName = binding.editTextLastName.text.toString()
            val inputEmail = binding.editTextEmail.text.toString()
            val inputPassword = binding.editTextPassword.text.toString()

            if (inputFirstName.isEmpty() || inputLastName.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty()) {

                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    firstName = inputFirstName,
                    lastName = inputLastName,
                    email = inputEmail,
                    password = inputPassword,
                )
                register(user)
                saveUserData(user)
                listener?.onButtonCLick(fragment, "ONBOARDING")
            }
        }

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun register(user: User) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.service.registerUser(user)
                if (response.isSuccessful) {
                    val str = response.body()
                    if (str != null) {
                        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("APISERVICE", "Response body is null")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        response.errorBody()?.string(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("APISERVICE", "Error fetching data", e)
            }
        }
    }

    private fun saveUserData(user: User) {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", user.email)
        editor.apply()
    }

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}