package com.mohamed.data.ds

import com.mohamed.domain.entity.CityEntity

class TrieNode {
    private val children: MutableMap<Char, TrieNode> = mutableMapOf()
    private val cities: MutableList<CityEntity> = mutableListOf()


    fun buildTrie(cities: List<CityEntity>) {
        cities.forEach { city ->
            insert(city)
        }
    }


    fun insert(city: CityEntity) {
        val key = city.displayName.lowercase()
        var current = this

        for (char in key) {
            current = current.children.getOrPut(char) { TrieNode() }
        }

        current.cities.add(city)
    }


    fun search(prefix: String): List<CityEntity> {
        val key = prefix.lowercase()
        var current = this

        for (char in key) {
            current = current.children[char] ?: return emptyList()
        }

        return collectAllCities(current)
    }


    private fun collectAllCities(startNode: TrieNode): List<CityEntity> {
        val result = mutableListOf<CityEntity>()
        val visited = mutableSetOf<String>()
        val stack = ArrayDeque<TrieNode>()
        stack.add(startNode)

        while (stack.isNotEmpty()) {
            val node = stack.removeLast()

            for (city in node.cities) {
                if (visited.add(city.displayName.lowercase())) {
                    result.add(city)
                }
            }

            stack.addAll(node.children.values)
        }

        return result
    }
}