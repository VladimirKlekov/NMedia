package ru.netology.nmedia.interfaces

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll():List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id:Long)
}