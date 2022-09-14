package ru.netology.nmedia.viewmodel

//ВЕРСИЯ ДЛЯ КЛИЕНТ_СЕРВЕР
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.FeedModel.FeedModel
import ru.netology.nmedia.databases.AppDbRoom
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfaces.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.singleLiveEvent.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

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

    private val repository: PostRepository = PostRepositoryImpl(
//        AppDbRoom.getInstance(context = application).postDao()
    )


    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    private val _postCreate = SingleLiveEvent<Unit>()
    val postCreate: LiveData<Unit>
        get() = _postCreate


    val edited = MutableLiveData(empty)


//_______________________________________________________________________________________________//

    init {
        load()
    }

    fun load() {
        thread {
            //начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            //данные успешно получены
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
//получена ошибка
                FeedModel(error = true)
            }.also { _data::postValue }
        }
    }

    //________________________________________________________________________________________________//
    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                _postCreate.postValue(Unit)
            }
            edited.postValue(empty)
        }
    }
//________________________________________________________________________________________________//


    fun textStorage(value: String) = repository.textStorage(value)

    fun textStorageDelete() = repository.textStorageDelete()

    fun likeById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) = repository.shareById(id)

    fun eye() = repository.eye()

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


    fun edit(post: Post) {
        edited.value = post
    }
}