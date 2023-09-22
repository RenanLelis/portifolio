package com.renan.webstore.business;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.List;

public interface JwtService {
    
    static final String AUTH = "AUTHORIZATION";
    
    static final String       API_KEY             = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng==";
    static final String       CLAIMS_ID_USER      = "USER";
    static final String       CLAIMS_EMAIL        = "EMAIL";
    static final String       CLAIMS_STATUS    = "STATUS";
    static final String       CLAIMS_AUTHORIZED   = "AUTHORIZED";
    static final String       CLAIMS_FIRST_NAME   = "FIRST_NAME";
    static final String       CLAIMS_LAST_NAME    = "LAST_NAME";
    static final String       CLAIMS_ROLES    = "ROLES";
    static final Integer      TIMEOUT             = 2 * 60 * 60 * 1000;
    public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    
    String generateJWT(Integer idUser, Boolean active, String email, String firstName, String lastName, List<String> roles);
    
    String refreshJWT(String jwt) throws RuntimeException;
    
    Integer getIdUser(String jwt) throws RuntimeException;
    
    Claims validateJWT(String jwt) throws RuntimeException;
    
}
