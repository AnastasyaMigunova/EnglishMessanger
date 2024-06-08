package com.example.application.presentation.onboarding.testing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.data.model.Answer

class AnswersViewModel : ViewModel() {
    private val dataList: MutableLiveData<List<Answer>> by lazy {
        MutableLiveData<List<Answer>>(ArrayList())
    }

    fun getData(): MutableLiveData<List<Answer>> {
        return dataList
    }

    fun addItem(item: Answer) {
        val list: MutableList<Answer> = dataList.value?.toMutableList() ?: mutableListOf()
        list.add(item)
        dataList.value = list
    }

    fun removeItem(index: Int) {
        val list: MutableList<Answer> = dataList.value?.toMutableList() ?: mutableListOf()
        if (index in 0 until list.size) {
            list.removeAt(index)
            dataList.value = list
        }
    }
}
