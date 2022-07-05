package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

//репозиторий, который будет хранить в оперативной памяти
class InMemoryPostRepository:PostRepository {
    var post = Post(
        author = "Нетология. Университет интернет-профессий",
        authorAvatar = "",
        published = "21 мая в 18:36",
        content = "Привет учителям Нетологии!!! Я делаю свое первое приложение и пытюсь понять, что здесь и как. Пока все идет со скрипом, но я стараюсь. Как мне кажется, у меня немого получается. Если Вы хотите увидеть, как я постигаю азы пограммирования, то нажмите на ссылку ниже. Буду признателен за критику и Ваши советы.",
        likedByMe=false
    )

    override fun Share() {
        post = post.copy(
            share = post.share+1)
        data.value=post
    }

    override fun Eye() {
        post = post.copy(
            eye = post.eye+1)
        data.value=post
    }

    private val data = MutableLiveData(post)
    //возврщать подписку на пост
    override fun get(): LiveData<Post> = data

    override fun Like() {
        post = post.copy(
            likedByMe=!post.likedByMe,
        likes = if(post.likedByMe) post.likes-1 else post.likes+1)
        //отправляем обновления
        data.value=post

    }
}