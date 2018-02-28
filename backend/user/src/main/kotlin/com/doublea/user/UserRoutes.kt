package com.doublea.user

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

class UserRoutes(private val userHandler: UserHandler) {

    @Bean
    fun userRouter(): RouterFunction<ServerResponse> =
            router {
                ("/users" and accept(MediaType.APPLICATION_JSON_UTF8)).nest {
                    POST("/sign-up", userHandler::saveUser)
                }
            }
}