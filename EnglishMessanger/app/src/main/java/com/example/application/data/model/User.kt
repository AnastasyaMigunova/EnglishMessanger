package com.example.application.data.model

data class User (
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val email: String,
    val salt: String? = null,
    val password: String,
    val phone: String? = null,
    val dateOfBirth: String? = null,
    val photo: String? = null,
    val languageLevel: String? = null,
    val emailFriends: List<String>? = null
    ) {
    override fun toString(): String {
        return "User(id='$id', firstName='$firstName', lastName='$lastName', username='$username', email='$email')"
    }
}