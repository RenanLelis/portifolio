package com.renan.todo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Service for jwt Operations - JSON Web Token
 */
public interface JwtService {

    public static final String       API_KEY             = "07J0PpNnASqADC9LVooxIVREQs6jUq0yyvVpGVQXJkAPtwLZ7cMQSiBbT9aSv8O1n7q3YKbW2l1niOWCbcz3ng==";
    public static final String       CLAIMS_ID_USER      = "USER";
    public static final String       CLAIMS_EMAIL        = "EMAIL";
    public static final String       CLAIMS_ID_STATUS    = "STATUS";
    public static final String       CLAIMS_AUTHORIZED   = "AUTHORIZED";
    public static final String       CLAIMS_FIRST_NAME   = "FIRST_NAME";
    public static final String       CLAIMS_LAST_NAME    = "LAST_NAME";
    public static final Integer      TIMEOUT             = 2 * 60 * 60 * 1000;
    public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

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
                                     String lastName);

    /**
     * Generate new JWT validating the old
     *
     * @param jwt - the old token
     * @return - the new jwt token
     * @throws RuntimeException - in case of errors
     */
    public String refreshJWT(String jwt) throws RuntimeException;

    /**
     * get the user id from the jwt token
     *
     * @param jwt - the token
     * @return - the id of the user on the token
     * @throws RuntimeException - in case of errors
     */
    public Integer getIdUser(String jwt) throws RuntimeException;

    /**
     * Validate the JWT, sign, values and if is expired
     *
     * @param jwt - the token
     * @return true case is valid
     * @throws RuntimeException - in case of errors
     */
    public Claims validateJWT(String jwt) throws RuntimeException;

    /**
     * Validate the jwt token
     *
     * @param jwt - the token
     * @return - true if is valid
     * @throws RuntimeException - in case of errors
     */
    public Boolean validateJWTToken(String jwt) throws RuntimeException;

}