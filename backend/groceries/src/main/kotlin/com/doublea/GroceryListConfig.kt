package com.doublea

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.support.GenericApplicationContext

@Configuration
class GroceryListConfig(private val applicationContext: GenericApplicationContext) {

    @Primary
    @Bean
    fun groceryService(): GroceryListService = GroceryListServiceImpl(
            applicationContext.getBean(GroceryListRepository::class.java),
            applicationContext.getBean(GroceryItemRepository::class.java))
}