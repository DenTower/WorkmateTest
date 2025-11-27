package com.example.workmatetest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val person: Person,
    val contact: Contact,
    val location: Location,
    val pictureFilePath: String
)

@Serializable
data class Person(
    val firstname: String,
    val lastname: String,
    val gender: String,
    val age: String,
    val dateOfBirth: String,
    val nationality: String,
)

@Serializable
data class Contact(
    val phoneNumber: String,
    val email: String,
)

@Serializable
data class Location(
    val country: String,
    val state: String,
    val city: String,
    val street: String,
    val postCode: String,
    val timezone: String,
)