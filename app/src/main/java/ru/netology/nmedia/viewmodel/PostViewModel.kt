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
    0L,
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
            //Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            try {
                //Данные успешно получены
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                //Получена ошибка
                FeedModel(error = true)
            }.also(_data::postValue)
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

    //_______________________________________________________________________________________________//
    fun likeById(id: Long) {
        thread {
            repository.likeById(id)
//            _data.postValue(FeedModel(loading = true))
//            val oldPost = _data.value?.posts.orEmpty().find { it.id == id }
//            try {
//                if (oldPost != null) {
//                    if (!oldPost.likedByMe) {
//                        val newPost = repository.likeById(id)
//                    } else {
//                        repository.unlikeById(id)
//                    }
//                }
//                load()
//                _data.postValue(FeedModel(loading = false))
//            } catch (e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
        }
    }

    fun unlikeById(id: Long) {
        thread { repository.unlikeById(id) }
    }



    //fun likeById(id: Long) = repository.likeById(id)

//_______________________________________________________________________________________________//

    fun shareById(id: Long) = repository.shareById(id)

    fun eye() = repository.eye()

    //______________________________________________________________________________________________//
    //fun removeById(id: Long) = repository.removeById(id)
    fun removeById(id: Long) {
        thread {
// Оптимистичная модель
            val old = data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty().filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    //_______________________________________________________________________________________________//
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