package com.doublea

import java.util.*

interface GroceryListService {
    fun fetchGroceryLists(): List<GroceryList>
    fun fetchGroceryListsByUser(username: String): List<GroceryList>
    fun fetchGroceryList(id: Long): GroceryList?
    fun saveGroceryList(groceryList: GroceryList): GroceryList
    fun updateGroceryList(groceryList: GroceryList): Optional<GroceryList>
    fun deleteGroceryList(groceryList: GroceryList): Optional<GroceryList>
    fun fetchAllGroceryItems(): List<GroceryItem>
}

internal class GroceryListServiceImpl(
        private val groceryListRepository: GroceryListRepository,
        private val groceryItemRepository: GroceryItemRepository)
    : GroceryListService {

    override fun fetchGroceryLists(): List<GroceryList> =
            groceryListRepository.findAll().toList()

    override fun fetchGroceryListsByUser(username: String): List<GroceryList> =
            groceryListRepository.findAllByOwner(username)

    override fun fetchGroceryList(id: Long): GroceryList? =
            groceryListRepository.findById(id).orElse(null)

    override fun saveGroceryList(groceryList: GroceryList): GroceryList {
        groceryList.groceries.forEach {
            if (groceryItemRepository.existsById(it.groceryItem.name).not()) {
                groceryItemRepository.save(it.groceryItem)
            }
        }
        return groceryListRepository.save(groceryList)
    }

    override fun updateGroceryList(groceryList: GroceryList): Optional<GroceryList> =
            groceryListRepository.findById(groceryList.id).map { persistedGroceryList ->
                groceryListRepository.save(
                        persistedGroceryList.copy(
                                title = groceryList.title,
                                modifiedDate = Date(),
                                groceries = groceryList.groceries
                        ))

            }

    override fun deleteGroceryList(groceryList: GroceryList): Optional<GroceryList> {
        return try {
            groceryListRepository.deleteById(groceryList.id)
            Optional.of(groceryList)
        } catch (e: IllegalArgumentException) {
            Optional.empty()
        }
    }

    override fun fetchAllGroceryItems(): List<GroceryItem> =
            groceryItemRepository.findAll().toList()
}

