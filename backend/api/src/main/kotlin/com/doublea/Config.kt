package com.doublea

import com.doublea.handler.ApiHandler
import com.doublea.handler.ExceptionHandler
import com.doublea.route.ApiRoutes
import com.doublea.route.ViewRoutes
import com.doublea.security.securityBeans
import com.doublea.user.userBeans
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class Config

fun main(args: Array<String>) {
    val application = SpringApplication(Config::class.java)
    application.addInitializers(ApplicationContextInitializer<GenericApplicationContext> { ctx ->
        beans {
            bean { ViewRoutes(ref()) }
            bean { ApiHandler(ref(), ref()) }
            bean { ApiRoutes(ref()) }
            bean<ExceptionHandler>()
            userBeans()
            securityBeans { securityService ->
                pathMatchers("/hello", "/users/sign-up").permitAll()
                pathMatchers("/api/**").authenticated()
            }
        }.initialize(ctx)
    })
    application.run(*args)
}