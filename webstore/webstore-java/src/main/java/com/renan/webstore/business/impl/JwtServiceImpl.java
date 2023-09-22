package com.renan.webstore.business.impl;

import com.renan.webstore.business.AppErrorType;
import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.JwtService;
import com.renan.webstore.util.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    
    public String generateJWT(Integer idUser, Boolean active, String email, String firstName, String lastName, List<String> roles) {
        long nowMillis = System.currentTimeMillis();
        Date now       = new Date(nowMillis);
        long expMillis = nowMillis + TIMEOUT;
        Date exp       = new Date(expMillis);
        
        JwtBuilder builder = Jwts.builder().setIssuedAt(now);
        Map<String, Object> claims  = new HashMap<>();
        claims.put(CLAIMS_ID_USER, idUser);
        claims.put(CLAIMS_STATUS, active);
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_AUTHORIZED, true);
        claims.put(CLAIMS_FIRST_NAME, firstName);
        claims.put(CLAIMS_LAST_NAME, lastName);
        claims.put(CLAIMS_ROLES, roles);
        builder.setClaims(claims);
        builder.setExpiration(exp);
        builder = builder.signWith(getSignKey(), SIGNATURE_ALGORITHM);
        
        return builder.compact();
    }
    public String refreshJWT(String jwt) throws RuntimeException {
        Claims claims    = validateJWT(jwt);
        Integer idUser    = (Integer) claims.get(CLAIMS_ID_USER);
        Boolean active  = (Boolean) claims.get(CLAIMS_STATUS);
        String  email     = (String) claims.get(CLAIMS_EMAIL);
        String  firstName = (String) claims.get(CLAIMS_FIRST_NAME);
        String  lastName  = (String) claims.get(CLAIMS_LAST_NAME);
        List<String> roles = (List<String>) claims.get(CLAIMS_ROLES);
        return generateJWT(idUser, active, email, firstName, lastName, roles);
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
    
    public Claims validateJWT(String jwt) throws RuntimeException {
        String token = jwt.startsWith("Bearer") ? jwt.substring(7) : jwt;
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("JWT Validation Error " + e.getMessage());
        }
    }
    
    private static byte[] getBytesSignKey() {
        return API_KEY.getBytes();
    }
    
    private SecretKeySpec getSignKey() {
        return new SecretKeySpec(getBytesSignKey(), SIGNATURE_ALGORITHM.getJcaName());
    }
    
}
