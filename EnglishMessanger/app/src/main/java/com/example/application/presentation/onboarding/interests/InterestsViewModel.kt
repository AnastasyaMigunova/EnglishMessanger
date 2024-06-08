package com.example.application.presentation.onboarding.interests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.data.model.Interest

class InterestsViewModel : ViewModel() {
    private val dataList: MutableLiveData<List<Interest>> by lazy {
        MutableLiveData<List<Interest>>(ArrayList())
    }

    fun getData(): MutableLiveData<List<Interest>> {
        return dataList
    }

    fun addItem(item: Interest) {
        val list: MutableList<Interest> = dataList.value?.toMutableList() ?: mutableListOf()
        list.add(item)
        dataList.value = list
    }

    fun removeItem(index: Int) {
        val list: MutableList<Interest> = dataList.value?.toMutableList() ?: mutableListOf()
        if (index in 0 until list.size) {
            list.removeAt(index)
            dataList.value = list
        }
    }
}
