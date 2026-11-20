package com.yjotdev.clasificarpeces.data.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Provides
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import com.yjotdev.clasificarpeces.data.repository.SpeciesApiRepositoryImpl
import com.yjotdev.clasificarpeces.data.repository.SpeciesDaoRepositoryImpl
import com.yjotdev.clasificarpeces.data.repository.StringRepositoryImpl
import com.yjotdev.clasificarpeces.data.remote.network.RetrofitBuilder
import com.yjotdev.clasificarpeces.data.remote.api.SpeciesApi
import com.yjotdev.clasificarpeces.data.local.database.SpeciesDatabase
import com.yjotdev.clasificarpeces.data.local.dao.SpeciesDao
import com.yjotdev.clasificarpeces.domain.repository.SpeciesApiRepository
import com.yjotdev.clasificarpeces.domain.repository.SpeciesDaoRepository
import com.yjotdev.clasificarpeces.domain.repository.StringRepository
import com.yjotdev.clasificarpeces.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindSpeciesApiRepository(
        impl: SpeciesApiRepositoryImpl
    ): SpeciesApiRepository

    @Binds
    @Singleton
    abstract fun bindSpeciesDaoRepository(
        impl: SpeciesDaoRepositoryImpl
    ): SpeciesDaoRepository

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: StringRepositoryImpl
    ): StringRepository

    // --- PROVIDERS (Instancias externas) ---
    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit {
            return retrofitBuilder.getRetrofitInstance()
        }

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor { msm ->
                Log.d("Https", msm)
            }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        }

        @Provides
        @Singleton
        fun provideSpeciesApi(retrofit: Retrofit): SpeciesApi {
            return retrofit.create(SpeciesApi::class.java)
        }

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): SpeciesDatabase =
            Room.databaseBuilder(
                context,
                SpeciesDatabase::class.java,
                SpeciesDatabase.NAME
            ).build()

        @Provides
        @Singleton
        fun provideSpeciesDao(database: SpeciesDatabase): SpeciesDao =
            database.speciesDao()
    }
}