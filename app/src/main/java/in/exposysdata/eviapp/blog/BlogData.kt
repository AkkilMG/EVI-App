package `in`.exposysdata.eviapp.blog

data class BlogData(
    val title: String,
    val description: String,
    val image: String,
    val date: String,
    val content: String
)

data class BlogHomeData(
    val title: String,
    val description: String,
    val image: String
)