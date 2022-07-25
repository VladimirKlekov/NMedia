package ru.netology.nmedia.activity


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        run {
            // для записи нужен Editor
            val preferences = getPreferences(Context.MODE_PRIVATE)
            preferences.edit().apply(){
                putString("key", "value")
                commit() //commit - синхронно, aplly -асинхронно
            }
        }


        run {
            // для записи нужен Editor
            getPreferences(Context.MODE_PRIVATE)
                .getString("key", "no value")?.let{
                    Snackbar.make(binding.root, it, BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .show()
                }
            }



        val viewModel: PostViewModel by viewModels()

        val newPostContract = registerForActivityResult(NewPostActivityContract()) { text ->
            text?.let {
                viewModel.editContent(it)
                viewModel.save()
            }
        }

        val editPostActivityContract =
            registerForActivityResult(EditPostActivityContract()) { text ->
                text?.let {
                    viewModel.editContent(it)
                    viewModel.save()
                }
            }

        val adapter = PostAdapter(
            object : PostEventListener {

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    editPostActivityContract.launch(post.content)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }
                override fun onVideo(post: Post) {
                    val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intentVideo)
                }

            }
        )

        binding.container.adapter = adapter

        viewModel.data.observe(this)
        { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) binding.container.smoothScrollToPosition(0)
            }
        }

        binding.addPost.setOnClickListener {
            newPostContract.launch()
        }
    }
}


//        viewModel.edited.observe(this) { edited ->
//            binding.group.isVisible = edited.id != 0L
//            binding.supportEdit.hint = edited.content
//            if (edited.id == 0L) {
//                return@observe
//            }
////            binding.content.setText(edited.content)
////            binding.content.requestFocus()
//        }
//
//        binding.cancelEdit.setOnClickListener {
//            binding.group.isInvisible = true
//            AndroidUtils.hideKeyboard(binding.cancelEdit)
//            viewModel.save()
////            binding.content.clearFocus()
////            binding.content.setText("")
//        }
//        binding.save.setOnClickListener {
//            if (binding.content.text.isNullOrBlank()) {
//                Toast.makeText(it.context, getString(R.string.empty_post_error), Toast.LENGTH_SHORT)
//                    .show()
//                return@setOnClickListener
//            }
//            val text = binding.content.text.toString()
//            viewModel.editContent(text)
//            viewModel.save()
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(binding.content)
//            binding.content.setText("")
//        }
//        binding.list.adapter = adapter
//        viewModel.data.observe(this) { posts ->
//
//            //с обновлением прокрутки
//            val newPost = posts.size > adapter.currentList.size
//            adapter.submitList(posts) {
//                if (newPost) {
//                    binding.list.smoothScrollToPosition(0)
//                }
//            }
//
//        }
//    }
//}

//val adapter= PostAdapter{
//            viewModel.likeById(it.id)
//            viewModel.shareById(it.id)
//        }
//        nLikeListener = { viewModel.likeById(it.id) },
//        onShareListener = { viewModel.shareById(it.id) }
//Для подписки на обновления LiveData используется метод observe(), который принимает
// объект типа LifecycleOwner и функциональный интерфейс Observer.
//Интерфейс LifecycleOwner реализуется классами Android компонентов, например
// AppCompatActivity, LifecycleService, Fragment.

//обновление данных
//MainActivity: AppCompatActivity() означает, что мы расширяем AppCompatActivity.
//AppCompatActivity - это класс из библиотеки appcompat e v7. Это библиотека совместимости,
// которая поддерживает перенос некоторых функций последних версий Android на старые устройства.
//onCreate -> Первый метод жизненного цикла, который мы переопределяем, это onCreate().
// Он вызывается при первом создании активности. Здесь наш xml-макет.
//savedInstanceState - это ссылка на объект пакета, который передается в метод onCreate для каждого
// действия Android. При особых обстоятельствах действия могут восстанавливаться до предыдущего
// состояния, используя данные, хранящиеся в этом пакете. Если нет доступных данных экземпляра,
// savedInstanceState будет иметь значение null.
//тип Bundle, который представляет собой набор пар “ключ — значение” и может быть использован
// для сохранения предыдущего состояния активити

//MainActivityBinding - это сгенерированный класс. Имя этого класса берется из имени layout
// файла (т.е. main_activity), плюс слово Binding. MainActivityBinding все знает о нашем
// layout: какие View там есть, какие переменные (variable) мы там указывали, и как все это
// связать друг с другом, чтобы данные из переменных попадали в View.
//LayoutInflater – это класс, который умеет из содержимого layout-файла создать View-элемент.
// Метод который это делает называется inflate. Есть несколько реализаций этого метода с
// различными параметрами. Но все они используют друг друга и результат их выполнения один – View.

//Компонент ViewModel — предназначен для хранения и управления данными, связанными с
// представлением, а заодно, избавить нас от проблемы, связанной с пересозданием активити
// во время таких операций, как переворот экрана








