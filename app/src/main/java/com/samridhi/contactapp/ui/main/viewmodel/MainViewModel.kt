package com.samridhi.contactapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samridhi.contactapp.network.model.Contact
import com.samridhi.contactapp.network.usecase.GetContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getContactUseCase:  GetContactUseCase

) : ViewModel() {
   val list  = mutableListOf<Contact>()
    var logTag = "@MainViewModel"

//    //private val _: MutableLiveData<Athlete> = MutableLiveData()
//    val athleteData: LiveData<Athlete> get() = _athleteData
    init {
        getContactData()
    }
    private fun getContactData() {
        viewModelScope.launch {
            val result = getContactUseCase.getContactsData()
            list.addAll(result.data)
            Log.d(logTag, "getAthleteData: $result")
        }
    }
}