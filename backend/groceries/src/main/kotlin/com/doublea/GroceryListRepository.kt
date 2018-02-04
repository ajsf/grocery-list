package com.doublea

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GroceryListRepository : CrudRepository<GroceryList, Long>

@Repository
interface GroceryItemRepository : CrudRepository<GroceryItem, String>