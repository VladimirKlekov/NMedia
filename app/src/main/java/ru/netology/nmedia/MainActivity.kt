package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.util.Locale.filter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            author = "Нетология. Университет интернет-профессий",
            authorAvatar = "",
            published = "21 мая в 18:36",
            content = "Привет учителям Нетологии!!! Я делаю свое первое приложение и пытюсь понять, что здесь и как. Пока все идет со скрипом, но я стараюсь. Как мне кажется, у меня немого получается. Если Вы хотите увидеть, как я постигаю азы пограммирования, то нажмите на ссылку ниже. Буду признателен за критику и Ваши советы.",
            countLike = 1_099,
            countShare = 1_100_000,
            countEye = 100


        )
binding.root.setOnClickListener {
    println("Клик работает")
}
        binding.avatarImageView.setOnClickListener {
            println("Клик аватара работает")
        }
        binding.textPost.text = post.content
        binding.published.text = post.published
        binding.author.text = post.author
        binding.like.setOnClickListener {
            post.liked = !post.liked
            binding.like.setImageResource(
                if (post.liked) {
                    R.drawable.ic_liked
                } else {
                    R.drawable.ic_like
                }
            )
            if (post.liked) {
                post.countLike++
            } else {
                post.countLike--

            }
            binding.countLikeTextView.text = post.roundingCount(post.countLike)

        }
        binding.shareImageButton.setOnClickListener{
            post.countShare++
            binding.accountShareTextView.text = post.roundingCount(post.countShare)

        }
        binding.eyeImageButton.setOnClickListener{
            binding.eyeTextView.text=post.roundingCount(post.countEye)
        }
    }
}




