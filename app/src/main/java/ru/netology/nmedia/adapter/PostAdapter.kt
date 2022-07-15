package ru.netology.nmedia.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat

//создал свой тип данных
//typealias OnLikeListener = (post: Post) -> Unit
//typealias OnShareListener = (post: Post) -> Unit
//typealias OnRemoveListener = (post: Post) -> Unit

//создаю своего слушателя вместо типов данных
interface PostEventListener {
    //перечисляю, что может случиться
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
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
            published.text = post.published
            content.text = post.content
            like.text = roundingCount(post.likes)
            share.text = roundingCount(post.share)
            countEye.text = roundingCount(post.eye)
            like.isChecked = post.likedByMe
//            like.setImageResource(
//                if (post.likedByMe) {
//                    R.drawable.ic_liked
//                } else {
//                    R.drawable.ic_like
//                }
//            )
            like.setOnClickListener {
                listener.onLike(post)
            }
            share.setOnClickListener {
                listener.onShare(post)
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

fun roundingCount(value: Long): String {
    var roundingCount: Double = 0.00
    var result: String = ""

    if (value < 1_000) {
        roundingCount = value.toDouble()
    } else if (value >= 1_000 && value < 1_000_000) {
        roundingCount = (value.toDouble() / 1_000)
    } else if (value >= 1_000_000) {
        roundingCount = (value.toDouble() / 1_000_000)
    }
    when (value) {
        in 0 until 1000 -> result = roundingCount.toLong().toString()
        in 1_000 until 1_000_000 -> result =
            DecimalFormat("#0.0").format(roundingCount).plus("k")
        else -> result = DecimalFormat("#0.0").format(roundingCount).plus("m")
    }
    return result
}
//Adapter - класс, отвечающий за предоставление View, соответствующего конкретному элементу в наборе данных (посту в нашем случае).
//Position - позиция элемента в Adapter'e.
//Index - индекс View в Layout'e.
//ViewHolder - класс, содержащий информацию о визуальном отображении конкретного элемента списка.
//Binding - процесс подготовки View для отображения данных, соответствующих определённой позиции в адаптере.