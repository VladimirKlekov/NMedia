package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.repository.PostRepositorySharedPrefsImpl

//заглушка
val empty = Post(
    0,
    "",
    "",
    "",
    "",
    false,
    0,
    0,
    0

)

class PostViewModel (application: Application): AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySharedPrefsImpl(application)
    val data = repository.getAll()
    //Хранилище для поста, который будет создан
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) = repository.shareById(id)

    fun eye() {repository.eye()}

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