package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository{
    fun get(): LiveData<Post>
    fun Like()
    fun Share()
    fun Eye()
}