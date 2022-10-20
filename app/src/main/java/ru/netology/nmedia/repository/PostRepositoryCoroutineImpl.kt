package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDaoCoroutine
import ru.netology.nmedia.dao.PostEntity
import ru.netology.nmedia.dao.toEntity
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.exception.ApiError
import ru.netology.nmedia.exception.NetworkException
import ru.netology.nmedia.exception.UnknownException
import ru.netology.nmedia.interfaces.PostRepositoryCoroutine
import java.io.IOException

class PostRepositoryCoroutineImpl(private val postDao: PostDaoCoroutine) : PostRepositoryCoroutine {

    override val data: LiveData<List<Post>> = postDao.getAll().map {
        it.map(PostEntity::toDto)
    }

    /** --------------------------------------------------------------------------------------- **/
    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    /** --------------------------------------------------------------------------------------- **/

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    override suspend fun removeById(id: Long) {
        try {
            val response = PostsApi.service.removeById(id)
            postDao.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    override suspend fun likeById(id: Long) {
        try {
            val response = PostsApi.service.likeById(id)
            postDao.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    override suspend fun unlikeById(id: Long) {
        try {
            val response = PostsApi.service.unlikeById(id)
            postDao.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    override fun shareById(id: Long) {
        postDao.shareById(id)
    }

    /** --------------------------------------------------------------------------------------- **/
    override fun eye(id: Long) {
        postDao.eye(id)
    }

    /** --------------------------------------------------------------------------------------- **/
    //хранение истории ввода при выходе из несохраненного поста
    private val textStorages = mutableListOf<String>()
    override fun textStorage(value: String) {
        textStorages.add(value)
    }

    /** --------------------------------------------------------------------------------------- **/
    override fun textStorageDelete(): String {
        val transferTex = textStorages.toString()
            .replace("[", "")
            .replace("]", "")
        textStorages.clear()
        return transferTex
    }
}