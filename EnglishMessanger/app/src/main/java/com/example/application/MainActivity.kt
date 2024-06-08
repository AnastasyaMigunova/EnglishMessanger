package com.example.application

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.databinding.ActivityMainBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.AuthFragment
import com.example.application.presentation.PrestartFragment
import com.example.application.presentation.onboarding.OnboardingFragment
import com.example.application.presentation.user.MainProfileFragment


class MainActivity : AppCompatActivity(), OnButtonClickListener {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val fragment = PrestartFragment.newInstance()
    private val profileFragment = MainProfileFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        if (sharedPreferences.contains("email")) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, profileFragment)
                .addToBackStack("PRESTART")
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("MAIN_PROFILE")
                .commit()
        }
    }

    override fun onButtonCLick(fragment: Fragment, name: String) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(name)
            .commit()
    }
}