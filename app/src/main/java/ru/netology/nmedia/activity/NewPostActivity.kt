package ru.netology.nmedia.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.AndroidUtils


class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.content.requestFocus()

        val contents = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.content.setText(contents)
        if (contents == null) {
            binding.supportGroup.isInvisible=true
        }

        binding.closeEditidButton.setOnClickListener {
            finish()
        }

        binding.save.setOnClickListener {

            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.empty_post_error),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_CANCELED)
            } else {
                val result = Intent().putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())
                setResult(RESULT_OK, result)
            }
            finish()
        }
    }
}

//
//binding.save.setOnClickListener {
//    if (binding.content.text.isNullOrBlank()) {
//        Toast.makeText(
//
//            this,
//            getString(R.string.empty_post_error),
//            Toast.LENGTH_SHORT
//        ).show()
//        setResult(AppCompatActivity.RESULT_CANCELED)
//
//    } else {
//        val result = Intent().putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())
//        val text = binding.content.text.toString()
//        setResult(AppCompatActivity.RESULT_OK, result)
//        viewModel.editContent(text)
//        viewModel.save()
//        binding.content.clearFocus()
//        AndroidUtils.hideKeyboard(binding.content)
//        binding.content.setText("")
//    }
//    finish()
//}
//}
//}
//val text = binding.content.text.toString()