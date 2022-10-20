package ru.netology.nmedia.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.CountLikeShare


//создаю своего слушателя вместо типов данных
interface PostEventListener {
    //перечисляю, что может случиться
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onVideo(post: Post)
    fun onPost(post: Post)
}

class PostAdapter(
    private val listener: PostEventListener,

    ) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostEventListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published.toString()
            content.text = post.content
            like.text = CountLikeShare.roundingCount(post.likes)
            share.text = CountLikeShare.roundingCount(post.share)
            countEye.text = CountLikeShare.roundingCount(post.eye)
            like.isChecked = post.likedByMe
//            like.setImageResource(
//                if (post.likedByMe) {
//                    R.drawable.ic_liked
//                } else {
//                    R.drawable.ic_like
//                }
//            )

            if (post.video == null) {
                binding.videoGroup.visibility = View.GONE
            } else {
                binding.videoGroup.visibility = View.VISIBLE
            }
            like.setOnClickListener {
                listener.onLike(post)
            }
            share.setOnClickListener {
                listener.onShare(post)
            }
            play.setOnClickListener {
                listener.onVideo(post)
            }
            video.setOnClickListener {
                listener.onVideo(post)
            }
            root.setOnClickListener {
                println("root clicked")
                listener.onPost(post)
            }

            val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            Glide.with(itemView)
                .load(url)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)

            val urlAttachment = "http://10.0.2.2:9999/images/${post.attachment?.url}"
            if (post.attachment != null) {
                attachment.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load(urlAttachment)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(attachment)
            } else {
                attachment.visibility = View.GONE
            }
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)//пункты меню
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            //если равен id
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}


class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any = Unit
}