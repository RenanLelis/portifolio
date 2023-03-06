package com.renan.todo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitie class for jwt Operations - JSON Web Token
 *
 * @author renan
 */
public class JwtUtil {
    public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public static final String API_KEY = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng==";
//    public static final String       API_PROPERTIE_NAME = "API_KEY";

    public static final String CLAIMS_ID_USER = "USER";
    public static final String CLAIMS_EMAIL = "EMAIL";
    public static final String CLAIMS_ID_STATUS = "STATUS";
    public static final String CLAIMS_AUTHORIZED = "AUTHORIZED";

    public static final Integer TIMEOUT = 2 * 60 * 60 * 1000; // Miliseconds -> 60 minutes * 2 = 2 hours

    /**
     * Generate a JWT Token signed
     *
     * @param idUser   - the User id
     * @param idStatus - user status
     * @return jwt
     */
    public static String generateJWT(Integer idUser, Integer idStatus, String email) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Key signingKey = new SecretKeySpec(API_KEY.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
        long expMillis = nowMillis + TIMEOUT;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder().setIssuedAt(now);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(CLAIMS_ID_USER, idUser);
        claims.put(CLAIMS_ID_STATUS, idStatus);
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_AUTHORIZED, true);
        builder.setClaims(claims);
        builder.setExpiration(exp);
        builder = builder.signWith(SIGNATURE_ALGORITHM, signingKey);

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }

    /**
     * Generate new JWT validating the old
     *
     * @param jwt - the old token
     * @return - the new jwt token
     * @throws UtilException - in case of errors
     */
    public static String refreshJWT(String jwt) throws UtilException {
        Claims claims = validateJWT(jwt);
        Integer idUser = (Integer) claims.get(CLAIMS_ID_USER);
        Integer idStatus = (Integer) claims.get(CLAIMS_ID_STATUS);
        String email = (String) claims.get(CLAIMS_EMAIL);
        return generateJWT(idUser, idStatus, email);
    }

    /**
     * get the user id from the jwt token
     *
     * @param jwt - the token
     * @return - the id of the user on the token
     * @throws UtilException - in case of errors
     */
    public static Integer getIdUser(String jwt) throws UtilException {
        Claims claims = validateJWT(jwt);
        if (!claims.containsKey(CLAIMS_ID_USER)) {
            throw new UtilException(MessageUtil.getErrorMessageToken());
        }
        return (Integer) claims.get(CLAIMS_ID_USER);
    }

    /**
     * Validate the JWT, sign, values and if is expired
     *
     * @param jwt - the token
     * @return true case is valid
     * @throws UtilException - in case of errors
     */
    public static Claims validateJWT(String jwt) throws UtilException {
        try {
            return Jwts.parser()
                    .setSigningKey(new SecretKeySpec(API_KEY.getBytes(), SIGNATURE_ALGORITHM.getJcaName()))
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new UtilException("JWT Validation Error " + e.getMessage());
        }
    }

    /**
     * Validate the jwt token
     *
     * @param jwt - the token
     * @return - true if is valid
     * @throws UtilException - in case of errors
     */
    public static Boolean validateJWTToken(String jwt) throws UtilException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(new SecretKeySpec(API_KEY.getBytes(), SIGNATURE_ALGORITHM.getJcaName()))
                    .parseClaimsJws(jwt).getBody();
            return claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS);
        } catch (Exception e) {
            throw new UtilException("JWT Validation Error " + e.getMessage());
        }
    }

}
