package com.example.swifttext.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val password2:String = ""

    ){
    override fun hashCode(): Int {
        var result = id.hashCode() ?: 0
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + password2.hashCode()
        return result
    }
}