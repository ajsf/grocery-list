package com.doublea.user

import org.springframework.data.repository.CrudRepository
import java.util.*

internal interface UserRepository : CrudRepository<GroceryUser, Long> {
    fun findOneByUsername(username: String): Optional<GroceryUser>
}