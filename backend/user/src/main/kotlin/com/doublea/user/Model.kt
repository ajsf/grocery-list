package com.doublea.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.User
import javax.persistence.*

@Entity
@Table(name = "groceryuser",
        uniqueConstraints = arrayOf((UniqueConstraint(columnNames = arrayOf("username")))))
data class GroceryUser(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "user_id", nullable = false, updatable = false)
        val id: Long? = null,

        @Column(nullable = false, unique = true)
        val username: String,

        @Column(nullable = false)
        val password: String,

        val active: Boolean = true,

        @ElementCollection(fetch = FetchType.EAGER)
        val roles: List<String> = listOf("USER")
) {
    fun toUserDetails(): UserDetails =
            User.withUsername(username)
                    .password(password)
                    .accountExpired(!active)
                    .accountLocked(!active)
                    .credentialsExpired(!active)
                    .disabled(!active)
                    .authorities(roles.map(::SimpleGrantedAuthority).toList())
                    .build()
}