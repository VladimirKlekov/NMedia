package ru.netology.nmedia.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post


//аннотацией указали что библиотека Room будет автоматически генерировать из класса
@Entity
data class PostEntity(
//аннотацией указали что id будет генерироваться автоматически
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    //val authorAvatar: String,
    val published: Long,
    val content: String,
    val likedByMe: Boolean,

    val likes: Long,
    val share: Long,
    var eye: Long,
    val video: String? = null
) {


    companion object {

        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.authorId,  dto.published, dto.content,
                dto.likedByMe, dto.likes, dto.share, dto.eye
            )
    }

    fun toDto() = Post(id, authorId, published, content, likedByMe, likes, share, eye)
}
fun List<PostEntity>.toDto() = map { it.toDto() }
fun List<Post>.toEntity() = map { PostEntity.fromDto(it) }