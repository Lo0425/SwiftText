package com.example.swifttext

import com.example.swifttext.utils.ValidationUtil
import junit.framework.TestCase
import org.junit.Test

class ValidationUtilTest {

    @Test
    fun `empty email should not pass the validation`(){
        TestCase.assertEquals(ValidationUtil.validateEmail(""), false)
    }

    @Test
    fun `should return false if @ is not included`(){
        TestCase.assertEquals(ValidationUtil.validateEmail("abc.com"), false)
    }

    @Test
    fun `email should not contains any special character other than @ and dot`(){
        TestCase.assertEquals(ValidationUtil.validateEmail("abc#$%aa@abc.com"), false)
    }

    @Test
    fun `should return false if email start with special number`(){
        TestCase.assertEquals(ValidationUtil.validateEmail(".abc@abc.com"), false)
    }



    @Test
    fun `user name should contains only alphanumeric characters`(){
        TestCase.assertEquals(ValidationUtil.validateUserName("%#kahyrul123"), false)
    }

    @Test
    fun `valid email address should pass the test`(){
        TestCase.assertEquals(ValidationUtil.validateEmail("khayrul@gmail.com"), true)
    }

    @Test
    fun `if user name contains only alphanumeric characters, it should pass the test`(){
        TestCase.assertEquals(ValidationUtil.validateUserName("joel123"), true)
    }

}