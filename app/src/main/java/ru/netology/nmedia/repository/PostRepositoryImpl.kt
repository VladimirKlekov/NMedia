package ru.netology.nmedia.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostEntity
import ru.netology.nmedia.dao.toDto
import ru.netology.nmedia.dao.toEntity
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.exception.ApiError
import kotlinx.coroutines.CancellationException
import ru.netology.nmedia.exception.NetworkError
import ru.netology.nmedia.exception.UnknownError
import ru.netology.nmedia.interfaces.PostRepository
import java.io.IOException


class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {

    /** -------добавляю для flow--------------------------------------------------------------- **/
    //выполнить операцию на другом потоке
    // создание асинхронного потока в функции flow
    override val data = postDao.getAll().map(List<PostEntity>::toDto).flowOn(Dispatchers.Default)

    //метод и бесконечный цикл для опроса сервера
    override fun getNewerCount(firstId: Long): Flow<Int> = flow {
        try {
            while (true) {
                val response = PostsApi.service.getNewer(firstId)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body =
                    response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(body.toEntity())
//                    .map {
//                it.copy(visibility = false)
//            }

                //передаем значение в поток. Благодаря этому внешний код сможет получит переданное
                // через emit() в поток значение и использовать его.
                //Будем получать количество непрочитанных постов с сервера
                emit(body.size)
                delay(10_000L)
            }
            //во время сетевого запроса или паузы  delay(10_000L) поток flow может быть отменен
            //и будет выбрашено исключение catch(e:CancellationException). Выдеялем "отмену" в это отдельное
            //исклюяение, что бы было понятно, что не ошибка

        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getNewPosts() {
        try {
            postDao.getVisibility()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
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
        throw NetworkError
    } catch (e: Exception) {
        throw UnknownError
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
    } catch (e: IOException) {
        throw NetworkError
    } catch (e: Exception) {
        throw UnknownError
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
        throw NetworkError
    } catch (e: Exception) {
        throw UnknownError
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
        throw NetworkError
    } catch (e: Exception) {
        throw UnknownError
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
        throw NetworkError
    } catch (e: Exception) {
        throw UnknownError
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