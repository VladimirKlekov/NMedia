package ru.netology.nmedia.interfaces

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository{

        fun getAll(): LiveData<List<Post>>
        fun likeById(id:Long)
        fun shareById(id:Long)
        fun eye()
        fun removeById(id:Long)
        fun save(post:Post)
        fun textStorage(value: String)
        fun textStorageDelete():String
}