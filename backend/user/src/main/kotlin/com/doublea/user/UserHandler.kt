package com.doublea.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class UserHandler(private val userService: UserService,
                  private val encoder: PasswordEncoder) {

    fun saveUser(req: ServerRequest): Mono<ServerResponse> = req.bodyToMono<GroceryUser>()
            .subscribeOn(Schedulers.parallel())
            .flatMap {
                val encodedPassword = encoder.encode(it.password)
                Mono.just(it.copy(password = encodedPassword))
            }.flatMap {
                ServerResponse.ok().body(Mono.just(userService.saveUser(it)))
            }
}