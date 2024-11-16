package com.samridhi.contactapp.di

import com.google.gson.GsonBuilder
import com.samridhi.contactapp.network.api.ContactsApi
import com.samridhi.contactapp.network.repository.MainRepository
import com.samridhi.contactapp.network.usecase.GetContactUseCase
import com.samridhi.contactapp.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideContactsApi(): ContactsApi =
    Retrofit.Builder()
      .baseUrl(Constant.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
      .build()
      .create(ContactsApi::class.java)

  @Singleton
  @Provides
  fun getContactUseCase(mainRepository : MainRepository) : GetContactUseCase = GetContactUseCase(mainRepository)
}