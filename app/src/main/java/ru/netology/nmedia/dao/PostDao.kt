package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.Entity.PostEntity
import ru.netology.nmedia.dto.Post


@Dao
interface PostDao {
    /** -------добавляю для flow--------------------------------------------------------------- **/
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE Visibility = 1 ORDER BY id DESC")
    fun getVisibility(): Flow<List<PostEntity>>

    /** --------------------------------------------------------------------------------------- **/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: Long, content: String)

    suspend fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("""UPDATE PostEntity SET share = share + 1   WHERE id = :id;""")
    fun shareById(id: Long)

    @Query("""UPDATE PostEntity SET eye = eye + 1   WHERE id = :id;""")
    fun eye(id: Long)

    fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
    fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
}