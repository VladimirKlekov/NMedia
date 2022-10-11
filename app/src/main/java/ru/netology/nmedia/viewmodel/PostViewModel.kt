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
            //try {
                //принимает функцию getAllAsync с анонимным объектом с интерфейсом PostRepository.GetAllCallback
               repository.getAllAsync(object : PostRepository.Callback<List<Post>>{
                    override fun onSuccess(posts: List<Post>) {
                        //в случае успеха буду создавать FeedModel
                        _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
                    }

                    override fun onError(e: Exception) {
                        //ошибка
                        _data.postValue(FeedModel(error = true))
                    }
                })

         }
    }

    //________________________________________________________________________________________________//
    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreate.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }
//________________________________________________________________________________________________//


    fun textStorage(value: String) = repository.textStorage(value)

    fun textStorageDelete() = repository.textStorageDelete()

    //_______________________________________________________________________________________________//
    fun likeById(id: Long) {
        repository.likeAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                _postCreate.postValue(Unit)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun unlikeById(id: Long) {

        repository.unlikeAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                _postCreate.postValue(Unit)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }


//_______________________________________________________________________________________________//

    fun shareById(id: Long) = repository.shareById(id)

    fun eye() = repository.eye()

    //______________________________________________________________________________________________//
    //fun removeById(id: Long) = repository.removeById(id)
    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }


        })

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