package com.samridhi.contactapp.network.repository

import com.samridhi.contactapp.network.api.ContactsApi
import javax.inject.Inject

class MainRepository
@Inject
constructor(private val api : ContactsApi){
    suspend fun getContactsData() = api.getContacts()
}
