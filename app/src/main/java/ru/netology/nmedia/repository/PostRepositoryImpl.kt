package ru.netology.nmedia.repository

//ВАРИАНТ PostRepositoryImpl ДЛЯ РАБОТЫ КЛИЕНТ-СЕРВЕРА

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfaces.PostRepository
import java.util.concurrent.TimeUnit

class PostRepositoryImpl() : PostRepository {

//________________________________________________________________________________________________//

    //    - Делаю объект с константой, в которой указываю Url нашего сервера
    companion object {
        private const val BASE_URL =
            "http://10.0.2.2:9999" //это вариант IP для встроенного эмулятора
        private val mediaType = "application/json".toMediaType()
    }

//________________________________________________________________________________________________//

// Делаю точку входа
    //Builder (строитель) - абстрактный класс/интерфейс, который определяет все этапы, необходимые
    // для производства сложного объекта-продукта.
//    OkHttp - это HTTP-клиент от Square для приложений Java и Android. Он предназначен для более
//    быстрой загрузки ресурсов и экономии пропускной способности.

    private val client = OkHttpClient.Builder()
        //устанавливаю время задержки 30 секунд
        .connectTimeout(30, TimeUnit.SECONDS)
        //соберем
        .build()

    //________________________________________________________________________________________________//
//Создал переменную gson, что бы с помощью библиотеки Gson() конвертировать объекты JSON в Java-объекты и наоборот
    private val gson = Gson()

    //________________________________________________________________________________________________//
//Получаю в переменную typeToken объект с правильным типом List<Post>
    private val typeToken = object : TypeToken<List<Post>>() {}

    //________________________________________________________________________________________________//
//Буду запрашивать данные с сервера и отображать их
    override fun getAll(): List<Post> {
        // Request.Builder() Конструктор для построения запросов на текстовые ссылки.
        val request: Request = Request.Builder()
//IP из переменной BASE_URL / интерфейс / медленный с 5 сек задеожкой / данные поста
//api/slow/post - берем из class PostController сервера
            .url("${BASE_URL}/api/slow/posts")
            //собрали
            .build()
//Возвращаю новый запрос
        return client.newCall(request)
            //выполнить
            .execute()
            //формирую временную область видимости для объекта и вызывают код, указанный в переданном лямбда-выражении.
            //получаем ответ в ввиде тела, который можно отобразить строкой, если ничего нет, выдаст ошибку
            .let {
                //Возвращает ненулевое значение.Органы реагирования должны быть закрыты и могут быть использованы только один раз.
                //Это всегда возвращает значение null для ответов, возвращаемых из ответа кэша, сетевого ответа и предыдущего ответа.
                it.body?.string() ?: throw RuntimeException("body is null")
            }
            //формирую временную область видимости для объекта и вызывают код, указанный в переданном лямбда-выражении.
            //
            .let {
                println()
                //метод fromJson() преобразует пришедший с сервера ответ в доступную для приложения форму
                gson.fromJson(it, typeToken.type)
            }
    }
    //________________________________________________________________________________________________//
    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(mediaType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
    }
    //хранение истории ввода при выходе из несохраненного поста
    private val textStorages = mutableListOf<String>()


    override fun likeById(id: Long) {
//        dao.likeById(id)
    }

    override fun shareById(id: Long) {
//        dao.shareById(id)
    }

    override fun eye() {
        //TODO
    }

    override fun removeById(id: Long) {
//        dao.removeById(id)
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