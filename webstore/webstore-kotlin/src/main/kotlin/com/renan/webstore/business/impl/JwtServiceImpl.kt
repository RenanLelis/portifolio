package com.renan.webstore.business.impl

import com.renan.webstore.business.*
import com.renan.webstore.util.getErrorMessageToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtServiceImpl : JwtService {

    @Throws(RuntimeException::class)
    override fun generateJWT(
        idUser: Int,
        active: Boolean,
        email: String,
        firstName: String,
        lastName: String?,
        roles: List<String>
    ): String {
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val expMillis: Long = nowMillis + TIMEOUT
        val exp = Date(expMillis)

        var builder = Jwts.builder().setIssuedAt(now)
        val claims: MutableMap<String, Any?> = HashMap()
        claims[CLAIMS_ID_USER] = idUser
        claims[CLAIMS_STATUS] = active
        claims[CLAIMS_EMAIL] = email
        claims[CLAIMS_AUTHORIZED] = true
        claims[CLAIMS_FIRST_NAME] = firstName
        claims[CLAIMS_LAST_NAME] = lastName
        claims[CLAIMS_ROLES] = roles
        builder.setClaims(claims)
        builder.setExpiration(exp)
        builder = builder.signWith(getSignKey(), SIGNATURE_ALGORITHM)

        return builder.compact()
    }

    @Throws(RuntimeException::class)
    override fun refreshJWT(jwt: String): String {
        val claims = validateJWT(jwt)
        val idUser = claims[CLAIMS_ID_USER] as Int
        val active = claims[CLAIMS_STATUS] as Boolean
        val email = claims[CLAIMS_EMAIL] as String
        val firstName = claims[CLAIMS_FIRST_NAME] as String
        val lastName = claims[CLAIMS_LAST_NAME] as String?
        val roles = claims[CLAIMS_ROLES] as List<String>
        return generateJWT(idUser, active, email, firstName, lastName, roles)
    }

    @Throws(RuntimeException::class)
    override fun getIdUser(jwt: String): Int {
        val claims = validateJWT(jwt)
        if (!claims.containsKey(CLAIMS_ID_USER)) {
            throw BusinessException(
                message = getErrorMessageToken(),
                businessMessage = true,
                errorType = AppErrorType.NOT_ALLOWED
            )
        }
        return claims[CLAIMS_ID_USER] as Int
    }

    @Throws(RuntimeException::class)
    override fun validateJWT(jwt: String): Claims {
        val token = if (jwt.startsWith("Bearer")) jwt.substring(7) else jwt
        return try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).body
        } catch (e: Exception) {
            throw java.lang.RuntimeException("JWT Validation Error " + e.message)
        }
    }

    private fun getBytesSignKey(): ByteArray {
        return API_KEY.encodeToByteArray()
    }

    private fun getSignKey(): SecretKeySpec {
        return SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.jcaName)
    }
}