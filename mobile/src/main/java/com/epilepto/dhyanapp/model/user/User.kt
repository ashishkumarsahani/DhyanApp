package com.epilepto.dhyanapp.model.user

import com.epilepto.dhyanapp.utils.DateUtils

data class User(
    val userId: String="",
    val username: String="",
    val userEmail: String="",
    val gender: String = "",
    val age:Int= 0,
    val goal:String = "",
    val addedAt: Long = System.currentTimeMillis()
){
    override fun toString(): String {
        return "userId: $userId\nusername: $username\nuserEmail: $userEmail\n" +
                "gender: $gender\nage: $age\n" +
                "goal: $goal\n" +
                "addedAt: ${
                    DateUtils.localDateToString(
                    DateUtils.longToLocalDate(addedAt)
                )}\n"
    }
}

fun Map<String, Any>.toUser(): User {
    return User(
        userId = this["userId"]!! as String,
        username = this["username"]!! as String,
        userEmail = this["userEmail"]!! as String,
        gender = this["gender"]!! as String,
        age = (this["age"]!! as Long).toInt(),
        addedAt = this["addedAt"]!! as Long,
        goal = this["goal"]!! as String
    )
}

