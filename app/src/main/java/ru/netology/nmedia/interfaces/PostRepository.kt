package ru.netology.nmedia.interfaces

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    /** -------добавляю для flow--------------------------------------------------------------- **/
    //выполнить операцию на другом потоке
    val data: Flow<List<Post>>
    //делаю новую функцию для получения информации, сколько постов не прочитано
    //firstId:Long - это id первого поста
    fun getNewerCount(firstId:Long):Flow<Int>
    suspend fun getNewPosts()
    /** --------------------------------------------------------------------------------------- **/

//    val data: LiveDate<List<Post>>
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

