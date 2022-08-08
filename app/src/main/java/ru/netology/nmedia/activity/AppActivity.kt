package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//____________________________________________________________________________________________//
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
//____________________________________________________________________________________________//
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
//____________________________________________________________________________________________//
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNullOrBlank()!=true) {


//                Snackbar.make(binding.root, "Content can't be empty", LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok) {
//                        finish()
//                    }
//                    .show()
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)

            findNavController(R.id.nav_main).navigate(R.id.action_feedFragment_to_newPostFragment)
            Bundle().apply {
                    textArg = text

//            findNavController(R.id.navigation).navigate(
//                R.id.action_feedFragment_to_newPostFragment,
//                Bundle().apply {
//                    textArg = text
                }

        }
//

//        supportFragmentManager.beginTransaction()
//            .add(R.id.nav_main, PostFragment.newInstance("1", "2")).commit()
}

}