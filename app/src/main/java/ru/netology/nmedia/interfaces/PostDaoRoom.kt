package ru.netology.nmedia.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dao.PostEntity


//аннотация указыват, что реализация будет на основании анотаций что будут над функциями
@Dao
interface PostDaoRoom {

    //@ЗАПРОС( "ВЫБЕРИТЕ * ИЗ PostEntity ЗАКАЗ по id DESC
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    //@ВСТАВКА
    @Insert
    fun insert(post: PostEntity)

    //@ЗАПРОС("ОБНОВИ PostEntity НАБОР content = :content ГДЕ id = :id
    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)
    //если id поста = 0 вставь (пост) иначе обновляем контент по id

    @Query(
        """
           UPDATE PostEntity SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id
        """
    )
    fun likeById(id: Long)

    //@ЗАПРОС("УДАЛИ В PostEntity по id = :id
    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query(
    """
           UPDATE PostEntity SET
               share = share + 1   WHERE id = :id;
        """)
    fun shareById(id: Long)


    @Query(
        """
           UPDATE PostEntity SET
               eye = eye + 1   WHERE id = :id;
        """)
    fun eye(id: Long)
}
