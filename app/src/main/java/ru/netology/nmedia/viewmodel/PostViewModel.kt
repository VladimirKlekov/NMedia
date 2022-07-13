package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

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

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun eye() {
        repository.eye()
    }

    fun removeById(id: Long) = repository.removeById(id)

    //Хранилище для поста, который будет создан
    val edited = MutableLiveData(empty)

    fun editContent(content: String) {
        edited.value?.let {
            repository.save(it)
            val trimmed = content.trim()
            if (trimmed == it.content) {

                return
            }
            edited.value = it.copy(content = trimmed)
        }

    }

    fun save() {
        edited.value?.let {
            repository.save(it)
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post


    }


}