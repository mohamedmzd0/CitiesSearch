package com.mohamed.data.repo

import com.mohamed.data.ds.TrieNode
import com.mohamed.data.mapper.CityMapper
import com.mohamed.data.utils.AssetManager
import com.mohamed.data.utils.JsonParser
import com.mohamed.domain.entity.CityEntity
import com.mohamed.domain.repo.CityRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val assetManager: AssetManager,
    private val jsonParser: JsonParser,
    private val mapper: CityMapper,
    private val trie: TrieNode
) : CityRepository {
    private val cityChunks = mutableListOf<List<CityEntity>>()
    private val mutex = Mutex()

    override suspend fun loadCities(value: String): List<CityEntity> {
        mutex.withLock {
            if (cityChunks.isEmpty()) {
                val inputStream = assetManager.openAssetStream("cities.json")
                val cityResponses = jsonParser.parseCitiesStream(inputStream)
                val mappedCities = mapper.map(cityResponses.toList())
                val allCities = mappedCities.sortedBy { it.displayName }
                cityChunks.clear()
                cityChunks.add(allCities)
                trie.buildTrie(allCities)
            }
        }
        return if (value.isEmpty())
            cityChunks.flatten().sortedBy { it.displayName }
        else
            trie.search(value).sortedBy { it.displayName }
    }


}

