package com.example.swifttext.utils

object ValidationUtil {

    fun validateEmail(email: String): Boolean {
        val emailReg = Regex(
            "[a-z\\d]{1,256}(\\.[a-z\\d]){0,25}" +
                    "@" +
                    "[a-z\\d]+[a-z\\d]{0,64}" +
                    "(\\.[a-z\\d][a-z\\d]{1,25})+", RegexOption.IGNORE_CASE)

        if (emailReg.matches(email)) {
            return true
        }

        return false
    }

    fun validateUserName(name :String): Boolean{
        if(Regex("[a-z\\d]{5,20}", RegexOption.IGNORE_CASE).matches(name)){
            return true
        }
        return false
    }

}