package com.example.application.presentation.user.dictionary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.data.model.Word

class DictionaryViewModel: ViewModel() {
    private val dataList: MutableLiveData<List<Word>> by lazy {
        MutableLiveData<List<Word>>(ArrayList())
    }

    fun getData(): MutableLiveData<List<Word>> {
        return dataList
    }

    fun addItem(item: Word) {
        val list: MutableList<Word> = dataList.value?.toMutableList() ?: mutableListOf()
        list.add(item)
        dataList.value = list
    }

}