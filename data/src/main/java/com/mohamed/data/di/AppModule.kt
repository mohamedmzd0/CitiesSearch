package com.mohamed.data.di

import android.content.Context
import com.mohamed.data.ds.TrieNode
import com.mohamed.data.mapper.CityMapper
import com.mohamed.data.repo.CityRepositoryImpl
import com.mohamed.data.utils.AssetManager
import com.mohamed.data.utils.JsonParser
import com.mohamed.domain.repo.CityRepository
import com.mohamed.domain.usecase.GetCitiesUseCase
import com.mohamed.domain.usecase.GetCitiesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJsonParser(): JsonParser = JsonParser()

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return AssetManager(context)
    }

    @Provides
    @Singleton
    fun provideCityRepository(
        assetManager: AssetManager, jsonParser: JsonParser, mapper: CityMapper, trieNode: TrieNode
    ): CityRepository {
        return CityRepositoryImpl(assetManager, jsonParser, mapper, trieNode)
    }

    @Provides
    @Singleton
    fun provideCityMapper() = CityMapper()

    @Provides
    @Singleton
    fun provideTrieNode() = TrieNode()

    @Provides
    @Singleton
    fun provideUseCase(repository: CityRepository): GetCitiesUseCase {
        return GetCitiesUseCaseImpl(repository)
    }

}