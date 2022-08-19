package `in`.exposysdata.eviapp

data class userModel (
    var db_userId: String? = null,
    var db_full_name: String? = null,
    var db_email: String? = null,
    var db_password: String? = null
)

data class User(
    val fullName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val admin: String? = "false"
)

data class Admin(
    val admin: String? = null
)

data class Applicant(
    val email: String? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val domain: String? = null,
    val date: String? = null
)