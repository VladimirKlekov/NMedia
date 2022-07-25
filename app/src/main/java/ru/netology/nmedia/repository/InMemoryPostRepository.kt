package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

//репозиторий, который будет хранить в оперативной памяти
class PostRepositoryInMemoryImpl : PostRepository {

    var posts = listOf(
        Post(
            id = 6,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "1 мая в 20:00",
            content = "Это первый пост",
            likedByMe = false,
            likes = 1_000_000,
            share = 1,
            eye = 1,
            video = "https://www.youtube.com/watch?v=5B__Rsk2Kd0",
        ),
        Post(
            id = 5,
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Это второй пост",
            likedByMe = false,
            likes = 100_000,
            share = 1,
            eye = 1
        ),
        Post(
            id = 4,
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
            id = 3,
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
            id = 2,
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
            content = "Это шестой пост",
            likedByMe = false,
            likes = 1,
            share = 1,
            eye = 1
        )
    )

    override fun eye() {
//       TODO
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        data.value = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = posts.firstOrNull()?.id?.plus(1) ?: 1L
                )
            ) + posts

        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        posts = data.value.orEmpty()
    }

    private val data = MutableLiveData(posts)

    //возврщать подписку на пост
    override fun getAll(): LiveData<List<Post>> = data

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                share = it.share + 1
            )
        }
        data.value = posts
    }

    //LiveData – это класс, который хранит данные и реализует паттерн Observable
    //

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
        data.value = posts
    }
}