package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfaces.PostDao
import ru.netology.nmedia.interfaces.PostRepository

class PostRepositorySQLiteImpl(
    private val dao:PostDao
): PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)
    private val textStorages = mutableListOf<String>()
    init {
        posts = dao.getAll()
        data.value = posts

    }

    override fun textStorage(value: String) {
        textStorages.add(value)
    }

    override fun textStorageDelete():String {
        var transferTex = textStorages.toString()
            .replace("[", "")
            .replace("]", "")
        textStorages.clear()
        return transferTex
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
        }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it
            else it.copy(
                share = it.share + 1
            )
        }
        data.value = posts

    }


    override fun eye() {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if(id ==0L) {
            listOf(saved) + posts
        }else{
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }


}


