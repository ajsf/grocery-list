package com.doublea.user

interface UserService {
    fun getUser(id: Long): GroceryUser?
    fun getUserByName(name: String): GroceryUser?
    fun saveUser(user: GroceryUser): GroceryUser
}

internal class UserServiceImpl
(private val userRepository: UserRepository) : UserService {

    override fun getUserByName(name: String): GroceryUser? =
            userRepository.findOneByUsername(name).orElse(null)

    override fun getUser(id: Long): GroceryUser? =
            userRepository.findById(id).orElse(null)

    override fun saveUser(user: GroceryUser) : GroceryUser = userRepository.save(user)
}
