package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

//ЛОГИРОВАНИЕ
private val client = OkHttpClient.Builder()
    //устанавливаю время задержки 30 секунд
    .connectTimeout(30, TimeUnit.SECONDS)
    //соберем
    .build()

//Для чего?
//private val okhttp = OkHttpClient.Builder()
//    .addInterceptor(logging)
//    .build()

//Настройка:
// 1.задаю BASE_URL IP эмулятора медленная версия
private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

//2.создать экземпляр Retrofit с BASEJJRL и Gson Converter'oM:
//Всё, что делает Converter - преобразует тело запроса/ответа в нужный формат
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    //.client(client)
    .baseUrl(BASE_URL)
    .build()

//описываю интерфейсы и создаю анотации
//Аннотации, обозначающие методы HTTP
//•	GET — получение ресурса (не имеет тела)
//•	POST - создание/обновление ресурса
//•	PUT — обновление ресурса
//•	PATCH - обновление ресурса (чаще всего частичное)
//•	DELETE — удаление ресурса
//•	HEAD, OPTIONS - дополнительные

interface PostApiService {
    //Call - это интерфейс, позволяющий выполнить запрос (sync/async)
    @GET("posts")
    fun getAll(): Call<List<Post>>

//Для отправки тела запроса есть маркерная аннотация (значит не имеющая элементов) @Body:
    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun dislikeById(@Path("id") id: Long): Call<Post>

    //Отдельно стоит отметить, что если мы не ждём ответа от сервиса, то стоит указывать именно
    // Call<Unit> (тогда сработает Converter, который закроет тело ответа):

    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long): Call<Unit>
}


//Для доступа к API создаётся Singleton с lazy-инициализацией поля:
//lazy() — это функция, которая принимает лямбду и возвращает экземпляр класса Lazy<T>,
// который служит делегатом для реализации ленивого свойства: первый вызов get() запускает
// лямбда-выражение, переданное lazy() в качестве аргумента, и запоминает полученное значение,
// а последующие вызовы просто возвращают вычисленное значение.
object PostApiServiceHolder {
    val service: PostApiService by lazy {
        retrofit.create()
    }
}

//У Retrofit есть несколько правил для работы с BASEJJRL:
//1.	BASEURL должен оканчиваться /
//2.	Если не писать в аннотациях метода начальный /, то адрес прибавляется к BASEURL:
//@GET("posts") = http://10.0.2.2/api/slow/pots
//3.	Если писать начальный слэш, то замещает весь путь: @GET("/posts") = http://10.0.2.2/DOSts
//4.	Если писать полный адрес без схемы, то схема остаётся, адрес меняется:
//@GET("//nmedia.dev/api/posts") = http://nmedia.dev/api/posts
//5.	Если писать полный адрес со схемой, то всё заменяется: @GET("https://nmedia.dev/api/posts") = https://nmedia.dev/api/posts