package com.doublea.route

import com.doublea.GroceryListService
import com.doublea.util.htmlView
import com.doublea.views.index
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

class ViewRoutes(private val groceryListService: GroceryListService) {

    @Bean
    fun viewRouter() =
            router {
                accept(MediaType.TEXT_HTML).nest {
                    GET("/hello") { req ->
                        val name = req.queryParam("name").orElse("User")
                        ServerResponse.ok()
                                .htmlView(Mono.just(
                                        index("Hello world")
                                ))
                    }
                }
            }
}