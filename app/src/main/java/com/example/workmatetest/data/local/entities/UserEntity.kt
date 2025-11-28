package com.example.workmatetest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workmatetest.domain.model.Contact
import com.example.workmatetest.domain.model.Location
import com.example.workmatetest.domain.model.Person
import com.example.workmatetest.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val person: Person,
    val contact: Contact,
    val location: Location,
    val pictureFilePath: String
) {
    fun toUser(): User {
        return User(
            id = id,
            person = person,
            contact = contact,
            location = location,
            pictureFilePath = pictureFilePath
        )
    }

}
