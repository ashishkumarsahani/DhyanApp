package com.epilepto.dhyanapp.utils


object SignInUtils {

    private val passRegex = Regex("^(?=.{8,32}\$)(?!-).*\$")

    fun verifySignUpDetails(name:String,email: String, password: String): String {
        val nameError = isValidName(name)
        return when {
            email.isBlank() -> "Email can't be empty"
            //To check valid email pattern
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> "Please provide a valid email address"

            (password.length < 8 || password.length > 30) && password.isNotBlank() -> {
                "The password must be at least 8 characters long and no more than 30 characters long."
            }
            !password.matches(passRegex)-> "Invalid password. Please enter a valid password"

            nameError.isNotEmpty() -> nameError

            else -> ""
        }
    }

    fun verifySignInDetails(email: String, password: String): String {
        return when {
            email.isBlank() -> "Email can't be empty"
            //To check valid email pattern
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> "Please provide a valid email address"

            (password.length < 8 || password.length > 30) && password.isNotBlank() -> {
                "The password must be at least 8 characters long and no more than 30 characters long."
            }
            !password.matches(passRegex)-> "Invalid password. Please enter a valid password"

            else -> ""
        }
    }


    fun isEmailValid(email:String):String = when{
        email.isBlank() -> "Email can't be empty"
        //To check valid email pattern
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() -> "Please provide a valid email address"

        else -> ""
    }

    fun isValidName(name: String, minLength: Int = 3, maxLength: Int = 30): String {
        // Define the regular expression pattern for valid names.
        val namePattern = "^[A-Za-z\\s]+$".toPattern()

        // Check if the name matches the pattern.
        if (!namePattern.matcher(name).matches()) {
            return "Name contains invalid characters."
        }

        if(name.length < minLength) return "Name is too small, Please use a valid name."
        if(name.length > maxLength) return "Name is too large, Please use a valid name."

        return ""
    }

}
