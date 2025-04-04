package com.cayot.flyingmore.fake.data.local.dao

import com.cayot.flyingmore.data.local.dao.AirportDao
import com.cayot.flyingmore.data.local.model.Airport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeAirportDao : AirportDao {
    private val _airports = MutableStateFlow<List<Airport>>(emptyList())

    override suspend fun insertAll(airports: List<Airport>) {
        _airports.update { currentList ->
            val updatedList = currentList.toMutableList()
            var nextId = currentList.maxOfOrNull { it.id } ?: 0

            airports.forEach { newAirport ->
                nextId += 1
                updatedList.add(newAirport.copy(id = nextId))
            }
            updatedList.toList()
        }
    }

    override suspend fun getAirport(id: Int): Airport? {
        return _airports.value.find { it.id == id }
    }

    override suspend fun getAirportByIataCode(iataCode: String): Airport? {
        return _airports.value.find { it.iataCode == iataCode }
    }

    override suspend fun searchAirportsByFullName(
        name: String,
        limit: Int
    ): List<Airport> {
        return _airports.value.filter {
            it.name.startsWith(name, ignoreCase = true)
        }.take(limit)
    }

    override suspend fun searchAirportsByIataCode(
        iataCode: String,
        limit: Int
    ): List<Airport> {
        return _airports.value.filter {
            it.iataCode.startsWith(iataCode, ignoreCase = true)
        }.take(limit)
    }

    fun getSize() : Int{
        return _airports.value.size
    }
}