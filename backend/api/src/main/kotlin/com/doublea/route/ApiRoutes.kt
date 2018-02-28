package com.doublea.route

import com.doublea.handler.ApiHandler
import com.doublea.util.WithLogging
import org.postgresql.jdbc.EscapedFunctions.LOG
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

class ApiRoutes(private val apiHandler: ApiHandler) : WithLogging() {

    @Bean
    fun apiRouter(): RouterFunction<ServerResponse> =
            router {
                ("/api/groceries".nest {
                    "/list".nest {
                        GET("/", apiHandler::getGroceryLists)
                        GET("/{id}", apiHandler::getGroceryList)
                        DELETE("/{id}", apiHandler::deleteGroceryList)
                        accept(MediaType.APPLICATION_JSON_UTF8).nest {
                            POST("/", apiHandler::saveGroceryList)
                            PUT("/{id}", apiHandler::updateGroceryList)
                        }
                    }
                    GET("/items", apiHandler::getGroceryItems)
                })
            }.filter { request, next ->
                LOG.debug(request)
                next.handle(request)
            }
}