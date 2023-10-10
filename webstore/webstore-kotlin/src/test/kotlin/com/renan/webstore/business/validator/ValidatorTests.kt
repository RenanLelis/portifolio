package com.renan.webstore.business.validator

import org.junit.jupiter.api.Test


class ValidatorTests {

    val validator = Validator()

    @Test
    fun itShouldValidateEmail(){
        val email = "renan.lelis@gmail.com"
        val result = validator.isMail(email)
        assert(result)
    }

    @Test
    fun itShouldValidateWrongEmail(){
        val email = "renan.lelis@gmail"
        val result = validator.isMail(email)
        assert(!result)
    }

    @Test
    fun itShouldCheckThatIsValidDate(){
        val dt = "2023-09-28"
        val result = validator.isValidDate(dt)
        assert(result)
    }

    @Test
    fun itShouldCheckThatIsInvalidDateByWrongInput(){
        val dt = "2asd023-09-28"
        val result = validator.isValidDate(dt)
        assert(!result)
    }

    @Test
    fun itShouldCheckThatIsInvalidDateByWrongFormat(){
        val dt = "28-09-2023"
        val result = validator.isValidDate(dt)
        assert(!result)
    }

    @Test
    fun itShouldCheckThatIsInvalidDateByWrongDay(){
        val dt = "2023-09-38"
        val result = validator.isValidDate(dt)
        assert(!result)
    }

    @Test
    fun itShouldCheckThatIsValidDateByShortYearNotation(){
        val dt = "23-09-28"
        val result = validator.isValidDate(dt)
        assert(result)
    }

    @Test
    fun itShouldCheckThatIsInvalidDateByWrongMonth(){
        val dt = "2023-15-38"
        val result = validator.isValidDate(dt)
        assert(!result)
    }

//    isValidDate
//
//    isValidPassword
//
//    isValidNewPasswordCode
//
//    isValidActivationCode

}