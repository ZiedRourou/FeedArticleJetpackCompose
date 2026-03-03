package com.example.feedarticlesjetpackcompose.di

import android.content.Context
import com.example.feedarticlesjetpackcompose.data.api.ApiInterface
import com.example.feedarticlesjetpackcompose.data.api.ApiRoutes
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesApi(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val moshi = Moshi.Builder().apply { add(KotlinJsonAdapterFactory()) }.build()

        return Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .client(client).build()
    }

    @Provides
    @Singleton
    fun getApi(): ApiInterface {
        return providesApi().create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): AuthSharedPref {
        return AuthSharedPref(context)
    }

}