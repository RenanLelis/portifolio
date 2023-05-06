package com.renan.todo.service

import com.renan.todo.util.API_KEY
import com.renan.todo.util.CLAIMS_AUTHORIZED
import com.renan.todo.util.CLAIMS_EMAIL
import com.renan.todo.util.CLAIMS_FIRST_NAME
import com.renan.todo.util.CLAIMS_ID_STATUS
import com.renan.todo.util.CLAIMS_ID_USER
import com.renan.todo.util.CLAIMS_LAST_NAME
import com.renan.todo.util.SIGNATURE_ALGORITHM
import com.renan.todo.util.TIMEOUT
import com.renan.todo.util.getErrorMessageToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.*
import javax.crypto.spec.SecretKeySpec
import org.springframework.stereotype.Service

/**
 * Service for jwt Operations - JSON Web Token
 */
@Service
class JwtServiceImpl : JwtService {

    /**
     * Generate a JWT Token signed
     *
     * @param idUser    - the User id
     * @param idStatus  - user status
     * @param email     - user email
     * @param firstName - user first name
     * @param lastName  - user last name
     * @return jwt
     */
    override fun generateJWT(
        idUser: Int?, idStatus: Int?, email: String?, firstName: String?,
        lastName: String?
    ): String? {
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val expMillis: Long = nowMillis + TIMEOUT
        val exp = Date(expMillis)
        var builder = Jwts.builder().setIssuedAt(now)
        val claims: MutableMap<String, Any?> = HashMap()
        claims[CLAIMS_ID_USER] = idUser
        claims[CLAIMS_ID_STATUS] = idStatus
        claims[CLAIMS_EMAIL] = email
        claims[CLAIMS_AUTHORIZED] = true
        claims[CLAIMS_FIRST_NAME] = firstName
        claims[CLAIMS_LAST_NAME] = lastName
        builder.setClaims(claims)
        builder.setExpiration(exp)
        builder = builder.signWith(getSignKey(), SIGNATURE_ALGORITHM)

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact()
    }

    /**
     * Generate new JWT validating the old
     *
     * @param jwt - the old token
     * @return - the new jwt token
     * @throws RuntimeException - in case of errors
     */
    @Throws(RuntimeException::class)
    override fun refreshJWT(jwt: String?): String? {
        val claims = validateJWT(jwt)
        val idUser = claims[CLAIMS_ID_USER] as Int?
        val idStatus = claims[CLAIMS_ID_STATUS] as Int?
        val email = claims[CLAIMS_EMAIL] as String?
        val firstName = claims[CLAIMS_FIRST_NAME] as String?
        val lastName = claims[CLAIMS_LAST_NAME] as String?
        return generateJWT(idUser, idStatus, email, firstName, lastName)
    }

    /**
     * get the user id from the jwt token
     *
     * @param jwt - the token
     * @return - the id of the user on the token
     * @throws RuntimeException - in case of errors
     */
    @Throws(BusinessException::class)
    override fun getIdUser(jwt: String?): Int? {
        val claims = validateJWT(jwt)
        if (!claims.containsKey(CLAIMS_ID_USER)) {
            throw BusinessException(
                getErrorMessageToken(),
                BUSINESS_MESSAGE,
                AppErrorType.NOT_ALLOWED
            )
        }
        return claims[CLAIMS_ID_USER] as Int?
    }

    /**
     * Validate the JWT, sign, values and if is expired
     *
     * @param jwt - the token
     * @return true case is valid
     * @throws RuntimeException - in case of errors
     */
    @Throws(RuntimeException::class)
    override fun validateJWT(jwt: String?): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt).body
        } catch (e: Exception) {
            throw RuntimeException("JWT Validation Error " + e.message)
        }
    }

    /**
     * Validate the jwt token
     *
     * @param jwt - the token
     * @return - true if is valid
     * @throws RuntimeException - in case of errors
     */
    @Throws(RuntimeException::class)
    override fun validateJWTToken(jwt: String?): Boolean? {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt).body
            claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS)
        } catch (e: Exception) {
            throw RuntimeException("JWT Validation Error " + e.message)
        }
    }

    /**
     * Get the byte array from the API Key
     *
     * @return - the byte array
     */
    private fun getBytesSignKey(): ByteArray {
        return API_KEY.encodeToByteArray()
    }

    /**
     * generate a sign key for a token
     *
     * @return - the key
     */
    private fun getSignKey(): SecretKeySpec {
        return SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.jcaName)
    }

}