package com.doublea.route

import com.doublea.handler.ApiHandler
import com.doublea.util.WithLogging
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

class ApiRoutes(private val apiHandler: ApiHandler) : WithLogging() {

    @Bean
    fun apiRouter(): RouterFunction<ServerResponse> =
            router {
                ("/api" and accept(MediaType.APPLICATION_JSON_UTF8)).nest {
                    "/groceries".nest {
                        GET("/list", apiHandler::getGroceryLists)
                        POST("/list", apiHandler::saveGroceryList)

                        GET("/list/{id}", apiHandler::getGroceryList)
                        PUT("/list/{id}", apiHandler::updateGroceryList)
                        DELETE("/list/{id}", apiHandler::deleteGroceryList)

                        GET("/items", apiHandler::getGroceryItems)
                    }
                }
            }.filter { request, next ->
                LOG.debug(request)
                next.handle(request)
            }
}