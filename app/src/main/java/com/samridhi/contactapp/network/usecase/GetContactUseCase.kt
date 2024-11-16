package com.samridhi.contactapp.network.usecase

import com.samridhi.contactapp.network.repository.MainRepository
import javax.inject.Inject

class GetContactUseCase
@Inject
constructor(private val mainRepository : MainRepository){
    suspend fun getContactsData() = mainRepository.getContactsData()
}