package com.doublea.user

import org.springframework.context.support.BeanDefinitionDsl

fun BeanDefinitionDsl.userBeans() {
    bean<UserService> { UserServiceImpl(ref()) }
    bean { UserRoutes(ref()) }
    bean { UserHandler(ref(), ref()) }
}