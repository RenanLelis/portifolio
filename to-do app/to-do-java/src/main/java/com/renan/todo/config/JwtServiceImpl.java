package com.renan.todo.config;

import com.renan.todo.util.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl {

    public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public final String CLAIMS_ID_USER = "USER";
    public final String CLAIMS_EMAIL = "EMAIL";
    public final String CLAIMS_ID_STATUS = "STATUS";
    public final String CLAIMS_AUTHORIZED = "AUTHORIZED";
    public final String CLAIMS_FIRST_NAME = "FIRST_NAME";
    public final String CLAIMS_LAST_NAME = "LAST_NAME";
    public final Integer TIMEOUT = 2 * 60 * 60 * 1000; // Miliseconds -> 60 minutes * 2 = 2 hours
    private final String SECRET_KEY = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng";

    private byte[] getBytesSignKey() {
        return SECRET_KEY.getBytes();
    }

    private SecretKeySpec getSignKey() {
        return new SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.getJcaName());
    }

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
    public String generateJWT(Integer idUser, Integer idStatus, String email, String firstName, String lastName) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expMillis = nowMillis + TIMEOUT;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder().setIssuedAt(now);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_ID_USER, idUser);
        claims.put(CLAIMS_ID_STATUS, idStatus);
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_AUTHORIZED, true);
        claims.put(CLAIMS_FIRST_NAME, firstName);
        claims.put(CLAIMS_LAST_NAME, lastName);
        builder.setClaims(claims);
        builder.setExpiration(exp);
        builder = builder.signWith(SIGNATURE_ALGORITHM, getSignKey());

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
        Claims claims = validateJWT(jwt);
        Integer idUser = (Integer) claims.get(CLAIMS_ID_USER);
        Integer idStatus = (Integer) claims.get(CLAIMS_ID_STATUS);
        String email = (String) claims.get(CLAIMS_EMAIL);
        String firstName = (String) claims.get(CLAIMS_FIRST_NAME);
        String lastName = (String) claims.get(CLAIMS_LAST_NAME);
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
            return Jwts.parser()
                    .setSigningKey(getSignKey())
                    .parseClaimsJws(jwt).getBody();
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
            Claims claims = Jwts.parser()
                    .setSigningKey(getSignKey())
                    .parseClaimsJws(jwt).getBody();
            return claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS);
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }


}
