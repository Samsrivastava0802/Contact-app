package com.samridhi.contactapp.network.api

import com.samridhi.contactapp.network.model.ContactData
import retrofit2.http.GET

interface ContactsApi {
    @GET("Host_api/Contacts.json")
    suspend fun getContacts(
    ): ContactData
}