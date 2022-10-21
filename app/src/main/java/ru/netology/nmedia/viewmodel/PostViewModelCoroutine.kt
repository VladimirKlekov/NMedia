package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.FeedModel.FeedModel
import ru.netology.nmedia.FeedModel.FeedModelState
import ru.netology.nmedia.databases.AppDbCoroutine
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfaces.PostRepositoryCoroutine
import ru.netology.nmedia.repository.PostRepositoryCoroutineImpl
import ru.netology.nmedia.singleLiveEvent.SingleLiveEvent


//заглушка
val empty = Post(
    0,
    "author",
    "",
    0L,
    "",
    false,
    0,
    0,
    0
)

/** ------------------------------------------------------------------------------------------ **/
class PostViewModelCoroutine(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepositoryCoroutine = PostRepositoryCoroutineImpl(
        AppDbCoroutine.getInstance(context = application).postDao()
    )

    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel> = repository.data.map {
        FeedModel(it, it.isEmpty())
        /** тоже самое **/
//            val data: LiveData<FeedModel>
//            get() = repository.data.map {
//                FeedModel(it,it.isEmpty())
    }

    private val _state = MutableLiveData<FeedModelState>()
    val state: MutableLiveData<FeedModelState>
        get() = _state

    private val edited = MutableLiveData(empty)

    private val _postCreate = SingleLiveEvent<Unit>()
    val postCreate: LiveData<Unit>
        get() = _postCreate

    /** --------------------------------------------------------------------------------------- **/
    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = FeedModelState.Loading

            try {
                repository.getAll()
                //в случае успеха буду создавать FeedModel
                _state.value = FeedModelState.Idle
            } catch (e: Exception) {
                _state.value = FeedModelState.Error
            }
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun save() {
        viewModelScope.launch {
            edited.value?.let {
                repository.save(it)
                _postCreate.postValue(Unit)
            }
            edited.value = empty
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun textStorage(value: String) = repository.textStorage(value)

    /** --------------------------------------------------------------------------------------- **/
    fun textStorageDelete() = repository.textStorageDelete()

    /** --------------------------------------------------------------------------------------- **/
    fun likeById(id: Long) {
        viewModelScope.launch {
            repository.likeById(id)
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun unlikeById(id: Long) {
        viewModelScope.launch {
            repository.unlikeById(id)
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun shareById(id: Long) = repository.shareById(id)

    /** --------------------------------------------------------------------------------------- **/
    fun eye(id: Long) = repository.eye(id)

    /** --------------------------------------------------------------------------------------- **/
    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
        }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun editContent(content: String) {
        // edited.value?.let {
        val trimmed = content.trim()
        if (edited.value?.content == trimmed) {
            return
        }
        edited.value = edited.value?.copy(content = trimmed)
        // }
    }

    /** --------------------------------------------------------------------------------------- **/
    fun edit(post: Post) {
        edited.value = post
    }
    /** --------------------------------------------------------------------------------------- **/
}