package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

//репозиторий, который будет хранить в оперативной памяти
class PostRepositoryInMemoryImpl : PostRepository {

    var posts = listOf(
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "1 мая в 20:00",
            content = "Это второй пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это второй пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это третий пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это четвертый пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это пятый пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это второй пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        )

    )


    override fun eye() {
//        post = post.copy(
//            eye = post.eye+1)
//        data.value=posts
    }

    private val data = MutableLiveData(posts)

    //возврщать подписку на пост
    override fun get(): LiveData<List<Post>> = data

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                share = it.share+ 1
            )
        }

        data.value=posts
    }

    //LiveData – это класс, который хранит данные и реализует паттерн Observable
    //

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(likedByMe = !it.likedByMe)
        }

        data.value = posts
    }
}