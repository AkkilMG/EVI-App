package `in`.exposysdata.eviapp.user

data class UserData(
    val admin: String?= null,
    val email: String? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null
)

data class ApplicantData (
    val email: String? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val domain: String? = null,
    val date: String? = null
)
