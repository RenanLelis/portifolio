package com.renan.webstore.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Random

const val LENGTH_ACTIVATION_CODE = 6
const val LENGTH_NEW_PASSWORD_CODE = 6
const val MIN_LENGTH_PASSWORD = 6
const val MAX_LENGTH_PASSWORD = 30
const val ROLE_PUBLIC = "PUBLIC"
const val ROLE_ADMIN = "ADMIN"
const val STATUS_ACTIVE = 1
const val STATUS_INACTIVE = 0

fun generateActivationCode(): String {
    return generateRandomString(LENGTH_ACTIVATION_CODE)
}

fun generateNewPasswordCode(): String {
    return generateRandomString(LENGTH_NEW_PASSWORD_CODE)
}

fun generateRandomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    val random = Random()
    val result = StringBuilder()
    for (i in 0 until length) {
        val r: Int = random.nextInt(chars.size - 1)
        result.append(chars[r])
    }
    return result.toString()
}

fun buildRole(roles: List<String>): String {
    val strb = StringBuilder("")
    roles.forEach { strb.append("$it,") }
    return strb.substring(0, strb.length -1)
}

fun buildRoles(roles: String): List<String>{
    return roles.split(",")
}

@Entity
@Table(name = "USER_WEBSTORE")
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Int?,
    @Column(name = "FIRST_NAME", nullable = false)
    var firstName: String,
    @Column(name = "LAST_NAME", nullable = true)
    var lastName: String?,
    @Column(name = "EMAIL", nullable = false, unique = true)
    val email: String,
    @Column(name = "USER_PASSWORD", nullable = false)
    var userPassword: String,
    @Column(name = "ACTIVATION_CODE", nullable = true)
    var activationCode: String?,
    @Column(name = "NEW_PASSWORD_CODE", nullable = true)
    var newPasswordCode: String?,
    @Column(name = "USER_STATUS", nullable = false)
    var userStatus: Int? = null,
    @Column(name = "USER_ROLES", nullable = false)
    var roles: String,
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val separatedRoles = roles.split(",")
        val authorities: ArrayList<GrantedAuthority> = ArrayList()
        separatedRoles.forEach { authorities.add(SimpleGrantedAuthority(it)) }
        return authorities.toMutableList()
    }

    override fun getPassword() = userPassword

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = userStatus == STATUS_ACTIVE

}