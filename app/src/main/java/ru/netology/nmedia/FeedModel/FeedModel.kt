package ru.netology.nmedia.FeedModel

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false
    )

/** интерфейс, чтобы подгружались посты, которые были написаны в предыдущю сессию------------ **/
sealed interface FeedModelState {
    object Idle : FeedModelState
    object Loading : FeedModelState
    object Refresh : FeedModelState
    object Error : FeedModelState
}