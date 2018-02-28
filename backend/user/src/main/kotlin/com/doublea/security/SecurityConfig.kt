package com.doublea.security

import com.doublea.user.UserService
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.anyExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono


fun BeanDefinitionDsl.securityBeans(paths: ServerHttpSecurity.AuthorizeExchangeSpec
.(SecurityService) -> ServerHttpSecurity.AuthorizeExchangeSpec) {
    bean<SecurityService> { SecurityServiceImpl() }
    bean<PasswordEncoder> { PasswordEncoderFactories.createDelegatingPasswordEncoder() }
    bean {
        ReactiveUserDetailsService { username ->
            ref<UserService>().getUserByName(username)
                    ?.toUserDetails()
                    ?.toMono()
                    ?: Mono.empty()
        }
    }
    bean<SecurityWebFilterChain> {
        ServerHttpSecurity.http()
                .authorizeExchange()
                .paths(ref())
                .pathMatchers("/api/groceries/list/{id}")
                .access { mono, context ->
                    mono.map { it.name == context.variables["username"] }
                            .map(::AuthorizationDecision)
                }
                .anyExchange().authenticated()
                .and().httpBasic()
                .and()
                .authenticationManager(ref())
                .formLogin()
                .and()
                .csrf().disable()
                .build()
    }
    bean {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(ref())
        authenticationManager.setPasswordEncoder(ref())
        authenticationManager
    }
}

