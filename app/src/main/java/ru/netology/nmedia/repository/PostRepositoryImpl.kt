package ru.netology.nmedia.repository

import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostEntity
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfaces.PostDaoRoom
import ru.netology.nmedia.interfaces.PostRepository

class PostRepositoryImpl(
    private val dao:PostDaoRoom
):PostRepository {

    private val textStorages = mutableListOf<String>()

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(it.id, it.author, it.authorAvatar,  it.published, it.content, it.likedByMe, it.likes, it.share, it.eye)
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun eye() {
        //TODO
    }

    override fun removeById(id: Long) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun textStorage(value: String) {
        textStorages.add(value)
    }

    override fun textStorageDelete(): String {
        var transferTex = textStorages.toString()
            .replace("[", "")
            .replace("]", "")
        textStorages.clear()
        return transferTex
    }
}