package ru.netology.nmedia.interfaces

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepositoryCoroutine {
    /** Для корутин **/

    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    fun shareById(id: Long)
    fun eye(id: Long)
    fun textStorage(value: String)
    fun textStorageDelete(): String
}

