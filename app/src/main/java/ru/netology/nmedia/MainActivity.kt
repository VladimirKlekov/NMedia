package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->

            with(binding) {
                textPost.text = post.content
                published.text = post.published
                author.text = post.author
                accountShareTextView.text = roundingCount(post.share)
                eyeTextView.text = roundingCount(post.eye)
                val likeImage = if (post.likedByMe) {
                    R.drawable.ic_liked
                } else {
                    R.drawable.ic_like
                }
                like?.setImageResource(likeImage)
                countLikeTextView?.text = roundingCount(post.likes)

            }

        }
        binding.like?.setOnClickListener {
            viewModel.like()
        }
        binding.shareImageButton.setOnClickListener {
            viewModel.share()
        }
       binding.eyeImageButton.setOnClickListener{
           viewModel.eye()
       }

    }

    fun roundingCount(value: Int): String {
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
            in 0 until 1000 -> result = roundingCount.toInt().toString()
            in 1_000 until 1_000_000 -> result =
                DecimalFormat("#0.0").format(roundingCount).plus("k")
            else -> result = DecimalFormat("#0.0").format(roundingCount).plus("m")
        }
        return result
    }


}


//binding.eyeImageButton.setOnClickListener {
//    binding.eyeTextView.text = post.roundingCount(post.countEye)
//}




