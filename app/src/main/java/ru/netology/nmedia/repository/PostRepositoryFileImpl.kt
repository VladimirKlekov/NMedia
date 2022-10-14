//package ru.netology.nmedia.repository
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.interfaces.PostRepository
//
//class PostRepositoryFileImpl(private val context: Context) : PostRepository {
//    private val gson = Gson()
//    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
//    private val filename = "posts.json"
//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)
//
//
//
//    init {
//        val file = context.filesDir.resolve(filename)
//        if (file.exists()) {
//            //если файл есть - читаем
//            context.openFileInput(filename).bufferedReader().use {
//                posts = gson.fromJson(it, type)
//                data.value = posts
//            }
//        } else
//        //если нет - записываем пустой массив
//            sync()
//    }
//
//    private fun sync() {
//        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
//            it.write(gson.toJson(posts))
//        }
//    }
//
//    override fun getAll(): LiveData<List<Post>> = data
//
//    override fun likeById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                likedByMe = !it.likedByMe,
//                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
//            )
//        }
//        data.value = posts
//        sync()
//    }
//
//    override fun shareById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                share = it.share + 1
//            )
//        }
//        data.value = posts
//        sync()
//    }
//
//    override fun eye() {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeById(id: Long) {
//        posts = posts.filter { it.id != id }
//        data.value = posts
//        sync()
//    }
//
//    override fun save(post: Post) {
//        data.value = if (post.id == 0L) {
//            listOf(
//                post.copy(
//                    id = posts.firstOrNull()?.id?.plus(1) ?: 1L
//                )
//            ) + posts
//
//        } else {
//            posts.map {
//                if (it.id != post.id) it else it.copy(content = post.content)
//            }
//        }
//        posts = data.value.orEmpty()
//        sync()
//    }
//
//    override fun textStorage(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun textStorageDelete(): String {
//        TODO("Not yet implemented")
//    }
//
//
//}