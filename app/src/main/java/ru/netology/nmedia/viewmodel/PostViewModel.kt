package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.databases.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

//заглушка
val empty = Post(
    0,
    "authorTest",
    "",
    "",
    "",
    false,
    0,
    0,
    0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao)

    val data = repository.getAll()

    //Хранилище для поста, который будет создан
    private val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun eye() {
        repository.eye()
    }

    fun removeById(id: Long) = repository.removeById(id)

    fun editContent(content: String) {
        edited.value?.let {
            val trimmed = content.trim()
            if (edited.value?.content == trimmed) {
                return
            }
            edited.value = edited.value?.copy(content = trimmed)
        }
    }

    fun save() {
        edited.value?.let {
            repository.save(it)

        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }
}