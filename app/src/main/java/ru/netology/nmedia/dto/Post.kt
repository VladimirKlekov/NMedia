package ru.netology.nmedia.dto


data class Post(
    val id: Long =0,
    val authorId: Long,
    //val authorAvatar: String,
    val published: Long,
    val content: String,
    val likedByMe: Boolean,

    val likes: Long,
    val share:Long,
    var eye:Long,
    val video: String? = null,

    val attachment: Attachment? = null

)

