package com.doublea.security

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authorization.AuthorizationContext
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

interface SecurityService {
    fun isAdmin(authentication: Mono<Authentication>, _ac: AuthorizationContext)
            : Mono<AuthorizationDecision>
}

internal class SecurityServiceImpl : SecurityService {

    override fun isAdmin(authentication: Mono<Authentication>,
                         _ac: AuthorizationContext): Mono<AuthorizationDecision> =
            authentication.flatMap {
                AuthorizationDecision(it.authorities
                        .map { it.authority }
                        .contains("ADMIN")).toMono()
            }
}