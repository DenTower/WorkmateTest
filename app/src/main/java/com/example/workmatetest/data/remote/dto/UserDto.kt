package com.example.workmatetest.data.remote.dto

import com.example.workmatetest.domain.model.Contact
import com.example.workmatetest.domain.model.Location
import com.example.workmatetest.domain.model.Person
import com.example.workmatetest.domain.model.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class UserDto(
    val results: List<ResultsDto>
) {
    fun toUser(pictureFilePath: String): User {
        val result = results.first()
        return User(
            person = Person(
                firstname = result.name.first,
                lastname = result.name.last,
                gender = result.gender,
                age = result.dob.age.toString(),
                dateOfBirth = result.dob.date,
                nationality = result.nat,
            ),
            contact = Contact(
                phoneNumber = result.phone,
                email = result.email
            ),
            location = Location(
                country = result.location.country,
                state = result.location.state,
                city = result.location.city,
                street = "${result.location.street.number} ${result.location.street.name}",
                postCode = result.location.postcode.jsonPrimitive.content,
                timezone = "${result.location.timezone.offset} ${result.location.timezone.description}"
            ),
            pictureFilePath = pictureFilePath
        )
    }
}

@Serializable
data class ResultsDto(
    val gender: String,
    val name: NameDto,
    val location: LocationDto,
    val email: String,
    val dob: DobDto,
    val phone: String,
    val nat: String,
    val picture: PictureDto
)

@Serializable
data class NameDto(
    val first: String,
    val last: String
)

@Serializable
data class LocationDto(
    val street: StreetDto,
    val city: String,
    val state: String,
    val country: String,
    val postcode: JsonElement,
    val timezone: TimezoneDto
)

@Serializable
data class StreetDto(
    val number: Int,
    val name: String
)

@Serializable
data class TimezoneDto(
    val offset: String,
    val description: String
)

@Serializable
data class DobDto(
    val date: String,
    val age: Int
)

@Serializable
data class PictureDto(
    val large: String
)
