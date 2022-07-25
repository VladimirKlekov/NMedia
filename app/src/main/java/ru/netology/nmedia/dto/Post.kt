package ru.netology.nmedia.dto


data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,

    val likes: Long,
    val share:Long,
    var eye:Long,
    val video: String? = null

    )


