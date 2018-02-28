package com.doublea

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GroceryListRepository : CrudRepository<GroceryList, Long> {
    fun findAllByOwner(username: String) : List<GroceryList>
}

@Repository
interface GroceryItemRepository : CrudRepository<GroceryItem, String>