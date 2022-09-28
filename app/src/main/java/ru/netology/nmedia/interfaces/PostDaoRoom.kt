package ru.netology.nmedia.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dao.PostEntity


//аннотация указыват, что реализация будет на основании анотаций что будут над функциями
@Dao
interface PostDaoRoom {
//________________________________________________________________________________________________//
    //@ЗАПРОС( "ВЫБЕРИТЕ * ИЗ PostEntity ЗАКАЗ по id DESC
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>
//________________________________________________________________________________________________//
    //@ВСТАВКА
    @Insert
    fun insert(post: PostEntity)
//_______________________________________________________________________________________________//
    //@ЗАПРОС("ОБНОВИ PostEntity НАБОР content = :content ГДЕ id = :id
    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)
//_______________________________________________________________________________________________//
    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)
    //если id поста = 0 вставь (пост) иначе обновляем контент по id
//_______________________________________________________________________________________________//
    @Query(
        """
           UPDATE PostEntity SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id
        """
    )
//_______________________________________________________________________________________________//
    fun likeById(id: Long)
//_______________________________________________________________________________________________//
    //@ЗАПРОС("УДАЛИ В PostEntity по id = :id
    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
//________________________________________________________________________________________________//
    @Query(
        """
           UPDATE PostEntity SET
               share = share + 1   WHERE id = :id;
        """)
//________________________________________________________________________________________________//
    fun shareById(id: Long)

//________________________________________________________________________________________________//
    @Query(
        """
           UPDATE PostEntity SET
               eye = eye + 1   WHERE id = :id;
        """)
//________________________________________________________________________________________________//
    fun eye(id: Long)
}
