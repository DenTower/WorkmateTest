package com.example.workmatetest.data.local.converters

import androidx.room.TypeConverter
import com.example.workmatetest.domain.model.Contact
import com.example.workmatetest.domain.model.Location
import com.example.workmatetest.domain.model.Person
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromPerson(person: Person): String {
        return json.encodeToString(person)
    }

    @TypeConverter
    fun toPerson(personString: String): Person {
        return json.decodeFromString(personString)
    }

    @TypeConverter
    fun fromContact(contact: Contact): String {
        return json.encodeToString(contact)
    }

    @TypeConverter
    fun toContact(contactString: String): Contact {
        return json.decodeFromString(contactString)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return json.encodeToString(location)
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        return json.decodeFromString(locationString)
    }
}
