package com.example.application.presentation.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.data.model.OnBoardingInfo
import com.example.application.databinding.FragmentPhotoBinding
import com.example.application.`interface`.OnSkipItemListener
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private val binding by viewBinding(FragmentPhotoBinding::bind)
    private var listener: OnSkipItemListener? = null

    fun setOnSkipItemListener(listener: OnSkipItemListener) {
        this.listener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewCamera.visibility = View.VISIBLE
        val image = binding.imageViewPhoto

        binding.textViewSkip.setOnClickListener {
            listener?.onSkipItemClick()
            setOnBoardingInfo(createOnBoardingInfo(""))
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { imageUri ->
                binding.imageViewCamera.visibility = View.GONE
                loadImage(imageUri)
            }
        }

        image.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadImage(imageUri: Uri) {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        var base64Image = ""
        bytes?.let { imageData ->
            base64Image = Base64.getEncoder().encodeToString(imageData)

            Glide.with(requireContext())
                .load(imageUri)
                .centerCrop()
                .into(binding.imageViewPhoto)

            setOnBoardingInfo(createOnBoardingInfo(base64Image))
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOnBoardingInfo(photo: String): OnBoardingInfo {
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val username = sharedPreferences.getString("username", "")
        val dateOfBirth = sharedPreferences.getString("dateOfBirth", "")

        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        var formattedDate: String? = null
        try {
            val date: Date? = dateOfBirth?.let { inputFormat.parse(it) }
            formattedDate = date?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            Log.e("PhotoFragment", "Error parsing date: ${e.message}")
        }

        return OnBoardingInfo(
            email = email,
            username = username,
            dateOfBirth = formattedDate,
            photo = photo
        )
    }

    private fun setOnBoardingInfo(onBoardingInfo: OnBoardingInfo) {
        lifecycleScope.launch {
            try {
                val serviceAnswer = ApiClient.service.setUserInfo(onBoardingInfo)
                Toast.makeText(requireContext(), serviceAnswer, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): PhotoFragment {
            return PhotoFragment()
        }
    }
}