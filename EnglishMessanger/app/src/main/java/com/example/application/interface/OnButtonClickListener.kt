package com.example.application.`interface`
import androidx.fragment.app.Fragment
import com.example.application.data.model.SentenceExercise

interface OnButtonClickListener {
    fun onButtonCLick(fragment: Fragment, name: String)
}