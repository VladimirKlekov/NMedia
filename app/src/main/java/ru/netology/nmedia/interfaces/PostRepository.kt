package ru.netology.nmedia.interfaces

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository{

        fun getAll(): List<Post>
        fun likeAsync(id: Long, callback: Callback<Post>)
        fun unlikeAsync(id: Long, callback: Callback<Post>)
        fun saveAsync(post: Post, callback: Callback<Post>)
        fun removeAsync(id: Long, callback: Callback<Post>)
        fun shareById(id:Long)
        fun eye()
        fun textStorage(value: String)
        fun textStorageDelete():String

        //Делаю асинхронный вызов функции

        //функция не имеет результат, но в качестве параметра принимает callback
        fun getAllAsync(callback: Callback<List<Post>>)
        //fun getAllAsync(callback: GetAllCallback)


        interface Callback<T> {
                fun onSuccess(posts: T)
                fun onError(e: Exception)
        }


//        interface GetAllCallback{
//                //функция для успешного результата. Возвращает список постов
//                fun onSuccess(post: List<Post>)
//                //функция ошибки. Возвращает Exception
//                fun onError(e:Exception)
//        }

}