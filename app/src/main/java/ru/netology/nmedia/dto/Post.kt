package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    var liked: Boolean = false,
    var countLike: Int,
    var countShare:Int=0

    ) {
    fun roundingCountLike():String{
        var roundingCountLike:String=""
        if(countLike<1000){
          roundingCountLike= countLike.toString()
        } else if(1000<=countLike || countLike<1000000){
            roundingCountLike= (countLike/1000).toString().plus("k")
        }else if(1000000<=countLike ){
            roundingCountLike= (countLike/1000000).toString().plus("m")
        }

        return roundingCountLike
    }
}

