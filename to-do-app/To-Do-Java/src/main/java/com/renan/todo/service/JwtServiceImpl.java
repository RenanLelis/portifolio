package com.renan.todo.service;


import com.renan.todo.util.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for jwt Operations - JSON Web Token
 */
@Service
public class JwtServiceImpl implements JwtService {

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
    public String generateJWT(Integer idUser, Integer idStatus, String email, String firstName,
                              String lastName) {
        long nowMillis = System.currentTimeMillis();
        Date now       = new Date(nowMillis);

        long expMillis = nowMillis + TIMEOUT;
        Date exp       = new Date(expMillis);

        JwtBuilder builder = Jwts.builder().setIssuedAt(now);
        Map<String, Object> claims  = new HashMap<>();
        claims.put(CLAIMS_ID_USER, idUser);
        claims.put(CLAIMS_ID_STATUS, idStatus);
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_AUTHORIZED, true);
        claims.put(CLAIMS_FIRST_NAME, firstName);
        claims.put(CLAIMS_LAST_NAME, lastName);
        builder.setClaims(claims);
        builder.setExpiration(exp);
        builder = builder.signWith(getSignKey(), SIGNATURE_ALGORITHM);

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Generate new JWT validating the old
     *
     * @param jwt - the old token
     * @return - the new jwt token
     * @throws RuntimeException - in case of errors
     */
    public String refreshJWT(String jwt) throws RuntimeException {
        Claims  claims    = validateJWT(jwt);
        Integer idUser    = (Integer) claims.get(CLAIMS_ID_USER);
        Integer idStatus  = (Integer) claims.get(CLAIMS_ID_STATUS);
        String  email     = (String) claims.get(CLAIMS_EMAIL);
        String  firstName = (String) claims.get(CLAIMS_FIRST_NAME);
        String  lastName  = (String) claims.get(CLAIMS_LAST_NAME);
        return generateJWT(idUser, idStatus, email, firstName, lastName);
    }

    /**
     * get the user id from the jwt token
     *
     * @param jwt - the token
     * @return - the id of the user on the token
     * @throws RuntimeException - in case of errors
     */
    public Integer getIdUser(String jwt) throws RuntimeException {
        Claims claims = validateJWT(jwt);
        if (!claims.containsKey(CLAIMS_ID_USER)) {
            throw new RuntimeException(MessageUtil.getErrorMessageToken());
        }
        return (Integer) claims.get(CLAIMS_ID_USER);
    }

    /**
     * Validate the JWT, sign, values and if is expired
     *
     * @param jwt - the token
     * @return true case is valid
     * @throws RuntimeException - in case of errors
     */
    public Claims validateJWT(String jwt) throws RuntimeException {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }

    /**
     * Validate the jwt token
     *
     * @param jwt - the token
     * @return - true if is valid
     * @throws RuntimeException - in case of errors
     */
    public Boolean validateJWTToken(String jwt) throws RuntimeException {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt).getBody();
            return claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS);
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }

    /**
     * Get the byte array from the API Key
     *
     * @return - the byte array
     */
    private static byte[] getBytesSignKey() {
        return API_KEY.getBytes();
    }

    /**
     * generate a sign key for a token
     *
     * @return - the key
     */
    private SecretKeySpec getSignKey() {
        return new SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.getJcaName());
    }

}