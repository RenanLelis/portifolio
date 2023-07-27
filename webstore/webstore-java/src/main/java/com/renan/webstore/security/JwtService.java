package com.renan.webstore.security;

import com.renan.webstore.service.AppErrorType;
import com.renan.webstore.service.BusinessException;
import com.renan.webstore.util.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final String       CLAIMS_ID_USER      = "USER";
    public static final String       CLAIMS_EMAIL        = "EMAIL";
    private static final String       CLAIMS_ID_STATUS    = "STATUS";
    private static final String       CLAIMS_AUTHORIZED   = "AUTHORIZED";
    private static final String       CLAIMS_FIRST_NAME   = "FIRST_NAME";
    private static final String       CLAIMS_LAST_NAME    = "LAST_NAME";
    public static final String       CLAIMS_ROLES    = "ROLES";
    private static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private int jwtExpiration;
    
    public int getJwtExpiration() {
        return jwtExpiration;
    }

    public String generateJWT(Integer idUser, Integer idStatus, String email, String firstName,
                              String lastName, List<String> roles) {
        long nowMillis = System.currentTimeMillis();
        Date now       = new Date(nowMillis);

        long expMillis = nowMillis + jwtExpiration;
        Date exp       = new Date(expMillis);

        JwtBuilder builder = Jwts.builder().setIssuedAt(now);
        Map<String, Object> claims  = new HashMap<>();
        claims.put(CLAIMS_ID_USER, idUser);
        claims.put(CLAIMS_ID_STATUS, idStatus);
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_AUTHORIZED, true);
        claims.put(CLAIMS_FIRST_NAME, firstName);
        claims.put(CLAIMS_LAST_NAME, lastName);
        claims.put(CLAIMS_ROLES, roles);
        builder.setClaims(claims);
        builder.setExpiration(exp);
        builder = builder.signWith(getSignKey(), SIGNATURE_ALGORITHM);

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public Claims validateJWT(String jwt) throws RuntimeException {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }

    public Boolean validateJWTToken(String jwt) throws RuntimeException {
        String token = jwt.startsWith("Bearer") ? jwt.substring(7) : jwt;
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
            return claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS);
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }

    public Integer getIdUser(String jwt) throws BusinessException {
        Claims claims = validateJWT(jwt);
        if (!claims.containsKey(CLAIMS_ID_USER)) {
            throw new BusinessException(
                    MessageUtil.getErrorMessageToken(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.NOT_ALLOWED);
        }
        return (Integer) claims.get(CLAIMS_ID_USER);
    }

    private byte[] getBytesSignKey() {
        return this.secretKey.getBytes();
    }

    private SecretKeySpec getSignKey() {
        return new SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.getJcaName());
    }
}
