package ru.netology.nmedia.dto

import java.text.DecimalFormat

data class Post(
    val id: Long = 0,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int=0,
    val share:Int = 0,
    var eye:Int = 0

    ) {





    }



