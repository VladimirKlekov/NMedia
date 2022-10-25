package ru.netology.nmedia.FeedModel

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false
    )

/** интерфейс, чтобы подгружались посты, которые были написаны в предыдущю сессию------------ **/

data class FeedModelState(
val refreshing: Boolean = false,
val error: Boolean = false,
val loading: Boolean = false)

//sealed interface FeedModelState {
//    object Idle : FeedModelState
//    object Loading : FeedModelState
//    object Refresh : FeedModelState
//    object Error : FeedModelState
//
//
//}